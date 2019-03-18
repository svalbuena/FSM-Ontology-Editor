package infrastructure.drawingpane

import infrastructure.drawingpane.shape._
import infrastructure.drawingpane.usecase.UseCaseContainer
import infrastructure.toolbox.ToolBox
import infrastructure.toolbox.section.item.fsm.{EndItem, StartItem, StateItem, TransitionItem}
import infrastructure.toolbox.section.selector.mouse.{DeleteMouseSelector, NormalMouseSelector}
import javafx.event.EventHandler
import javafx.scene.input.{MouseButton, MouseEvent}

class DrawingPaneController(toolBox: ToolBox, useCaseContainer: UseCaseContainer) {
  private var mouseX: Double = 0.0
  private var mouseY: Double = 0.0

  private var temporalTransition: Option[Transition] = None

  private val drawConnectableNodeUseCase = useCaseContainer.drawConnectableNodeUseCase
  private val dragConnectableNodeUseCase = useCaseContainer.dragConnectableNodeUseCase
  private val eraseConnectableNodeUseCase = useCaseContainer.eraseConnectableNodeUseCase
  private val drawTransitionUseCase = useCaseContainer.drawTransitionUseCase
  private val eraseTransitionUseCase = useCaseContainer.eraseTransitionUseCase
  private val linkDrawingPaneMouseClickedEventUseCase = useCaseContainer.linkDrawingPaneMouseClickedEventUseCase
  private val linkDrawingPaneMouseMovedEventUseCase = useCaseContainer.linkDrawingPaneMouseMovedEventUseCase

  val state1 = new State()
  val state2 = new State()

  addState(state1, 0, 0)
  addState(state2, 300, 0)

  state1.addAction("entry/Action 1")
  state1.addAction("entry/Action 1")
  state1.addAction("entry/Action 1")
  state1.addAction("entry/Action 1")

  linkDrawingPaneMouseClickedEventUseCase.link(drawingPaneMouseClickedListener)
  linkDrawingPaneMouseMovedEventUseCase.link(drawingPaneMouseMovedListener)

  private def drawingPaneMouseClickedListener: EventHandler[MouseEvent] = (event: MouseEvent) => {
    updateMousePosition(event)

    if (event.getButton == MouseButton.PRIMARY) {
      toolBox.getSelectedTool match {
        case _: TransitionItem =>
          if (temporalTransition.isDefined) {
            cancelTransitionCreation()
            toolBox.setToolToDefault()
          }
        case _: StateItem =>
          addState(new State(), event.getX, event.getY)
          toolBox.setToolToDefault()
        case _: StartItem =>
          addStart(new Start(), event.getX, event.getY)
          toolBox.setToolToDefault()
        case _: EndItem =>
          addEnd(new End(), event.getX, event.getY)
          toolBox.setToolToDefault()
        case _ =>
      }
    }
  }

  private def drawingPaneMouseMovedListener: EventHandler[MouseEvent] = (event: MouseEvent) => {
    toolBox.getSelectedTool match {
      case _: TransitionItem =>
        if (temporalTransition.isDefined) {
          val (deltaX, deltaY) = calculateDeltaFromMouseEvent(event)
          dragTemporalTransition(deltaX, deltaY)
        }
      case _ =>
    }

    updateMousePosition(event)
  }

  private def connectableNodeMouseClickedListener: EventHandler[MouseEvent] = (event: MouseEvent) => {
    event.consume()

    updateMousePosition(event)

    event.getSource match {
      case connectableNode: ConnectableNode =>
        if (event.getButton == MouseButton.PRIMARY) {
          toolBox.getSelectedTool match {
            case _: TransitionItem =>
              if (temporalTransition.isDefined) {
                establishTemporalTransition(connectableNode)
                toolBox.setToolToDefault()
              } else {
                val point = connectableNode.getLocalToParentTransform.transform(event.getX, event.getY)
                addTemporalTransition(connectableNode, point.getX, point.getY)
              }
            case _: DeleteMouseSelector =>
              eraseConnectableNodeUseCase.erase(connectableNode)
              toolBox.setToolToDefault()
            case _ =>
          }
        }
      case _ =>
    }
  }

  private def connectableNodeMouseDraggedListener: EventHandler[MouseEvent] = (event: MouseEvent) => {
    event.consume()

    event.getSource match {
      case connectableNode: ConnectableNode =>
        toolBox.getSelectedTool match {
          case _: NormalMouseSelector =>
            val (deltaX, deltaY) = calculateDeltaFromMouseEvent(event)
            dragConnectableNodeUseCase.drag(connectableNode, deltaX, deltaY)
          case _ =>
        }
      case _ =>
    }

    updateMousePosition(event)
  }

  private def addState(state: State, x: Double, y: Double): Unit = {
    state.setOnMouseClicked(connectableNodeMouseClickedListener)
    state.setOnMouseDragged(connectableNodeMouseDraggedListener)

    drawConnectableNodeUseCase.draw(state, x, y)
  }

  private def addStart(start: Start, x: Double, y: Double): Unit = {
    start.setOnMouseClicked(connectableNodeMouseClickedListener)
    start.setOnMouseDragged(connectableNodeMouseDraggedListener)

    drawConnectableNodeUseCase.draw(start, x, y)
  }

  private def addEnd(end: End, x: Double, y: Double): Unit = {
    end.setOnMouseClicked(connectableNodeMouseClickedListener)
    end.setOnMouseDragged(connectableNodeMouseDraggedListener)

    drawConnectableNodeUseCase.draw(end, x, y)
  }

  private def addTransition(transition: Transition): Unit = {
    drawTransitionUseCase.draw(transition)
  }

  private def addTemporalTransition(srcConnectableNode: ConnectableNode, x: Double, y: Double): Unit = {
    val ghostNode = new GhostNode()

    drawConnectableNodeUseCase.draw(ghostNode, x, y)

    val transition = new Transition(srcConnectableNode, ghostNode)

    ghostNode.addTransition(transition)

    addTransition(transition)

    temporalTransition = Some(transition)
  }

  private def establishTemporalTransition(dstConnectableNode: ConnectableNode): Unit = {
    val transition = temporalTransition.get
    val srcConnectableNode = transition.node1

    transition.node2 = dstConnectableNode

    srcConnectableNode.addTransition(transition)
    dstConnectableNode.addTransition(transition)

    eraseTransitionUseCase.erase(transition)
    drawTransitionUseCase.draw(transition)

    temporalTransition = None
  }

  private def dragTemporalTransition(deltaX: Double, deltaY: Double): Unit = {
    val transition = temporalTransition.get
    val ghostNode = transition.node2

    dragConnectableNodeUseCase.drag(ghostNode, deltaX, deltaY)
  }

  private def cancelTransitionCreation(): Unit = {
    val transition = temporalTransition.get
    val ghostNode = transition.node2

    eraseTransitionUseCase.erase(transition)
    eraseConnectableNodeUseCase.erase(ghostNode)

    temporalTransition = None
  }

  private def updateMousePosition(event: MouseEvent): Unit = {
    mouseX = event.getSceneX
    mouseY = event.getSceneY
  }

  private def calculateDeltaFromMouseEvent(event: MouseEvent): (Double, Double) = {
    (event.getSceneX - mouseX, event.getSceneY - mouseY)
  }
}
