package infrastructure.drawingpane

import infrastructure.drawingpane.shape._
import infrastructure.drawingpane.usecase.connectablenode.{DragConnectableNodeUseCase, DrawConnectableNodeUseCase, EraseConnectableNodeUseCase}
import infrastructure.drawingpane.usecase.transition.{DrawTransitionUseCase, RedrawTransitionUseCase}
import infrastructure.toolbox.ToolBox
import infrastructure.toolbox.section.item.fsm.{EndItem, StartItem, StateItem, TransitionItem}
import infrastructure.toolbox.section.selector.mouse.{DeleteMouseSelector, NormalMouseSelector}
import javafx.event.EventHandler
import javafx.scene.Node
import javafx.scene.input.{MouseButton, MouseEvent}
import javafx.scene.layout.Pane

class DrawingPane(toolBox: ToolBox) extends Pane {
  private var mouseX: Double = 0.0
  private var mouseY: Double = 0.0

  private var temporalTransition: Option[Transition] = None

  private val drawConnectableNodeUseCase = new DrawConnectableNodeUseCase(this)
  private val dragConnectableNodeUseCase = new DragConnectableNodeUseCase(this)
  private val eraseConnectableNodeUseCase = new EraseConnectableNodeUseCase(this)
  private val drawTransitionUseCase = new DrawTransitionUseCase(this)
  private val redrawTransitionUseCase = new RedrawTransitionUseCase(this)

  val state1 = new State()
  val state2 = new State()

  addState(state1, 0, 0)
  addState(state2, 300, 0)

  state1.addAction("entry/Action 1")
  state1.addAction("entry/Action 1")
  state1.addAction("entry/Action 1")
  state1.addAction("entry/Action 1")

  setOnMouseClicked(drawingPaneMouseClickedListener)
  setOnMouseMoved(drawingPaneMouseMovedListener)

  private def addState(state: State, x: Double, y: Double): Unit = {
    state.setOnMouseClicked(connectableNodeMouseClickedListener)
    state.setOnMouseDragged(connectableNodeMouseDraggedListener)

    drawConnectableNodeUseCase.draw(state, x, y)
  }

  private def addStart(start: Start, x: Double, y: Double): Unit ={
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
    addTransition(transition)

    temporalTransition = Some(transition)
  }

  private def establishTemporalTransition(dstConnectableNode: ConnectableNode): Unit = {
    val transition = temporalTransition.get
    val srcConnectableNode = transition.node1

    transition.node2 = dstConnectableNode

    srcConnectableNode.addTransition(transition)
    dstConnectableNode.addTransition(transition)

    redrawTransitionUseCase.redraw(transition)

    temporalTransition = None
  }

  private def drawingPaneMouseClickedListener: EventHandler[MouseEvent] = (event: MouseEvent) => {
    println("DrawingPaneMouseClickedListener")
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
            connectableNode.transitions.foreach(transition => redrawTransitionUseCase.redraw(transition))
          case _ =>
        }
      case _ =>
    }

    updateMousePosition(event)
  }



  private def dragTemporalTransition(deltaX: Double, deltaY: Double): Unit = {
    val transition = temporalTransition.get
    val ghostNode = transition.node2

    dragConnectableNodeUseCase.drag(ghostNode, deltaX, deltaY)
    redrawTransitionUseCase.redraw(transition)
  }

  private def cancelTransitionCreation(): Unit = {
    val transition = temporalTransition.get
    val tempNode = transition.node2

    eraseNode(transition)
    eraseNode(tempNode)

    temporalTransition = None
  }

  private def eraseNode(node: Node): Unit = {
    getChildren.remove(node)
  }

  private def updateMousePosition(event: MouseEvent): Unit = {
    mouseX = event.getSceneX
    mouseY = event.getSceneY
  }

  private def calculateDeltaFromMouseEvent(event: MouseEvent): (Double, Double) = {
    (event.getSceneX - mouseX, event.getSceneY - mouseY)
  }
}
