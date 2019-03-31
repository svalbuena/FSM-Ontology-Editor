package infrastructure

import java.awt.MenuBar

import infrastructure.drawingpane.DrawingPane
import infrastructure.drawingpane.shape._
import infrastructure.drawingpane.usecase.connectablenode._
import infrastructure.drawingpane.usecase.transition._
import infrastructure.elements.action.EntryAction
import infrastructure.elements.node.{ConnectableElement, End, GhostElement, Start, State}
import infrastructure.elements.transition.Transition
import infrastructure.menu.contextmenu.state.item.{AddEntryActionMenuItem, AddExitActionMenuItem}
import infrastructure.propertybox.PropertiesBox
import infrastructure.toolbox.ToolBox
import infrastructure.toolbox.section.item.fsm.{EndItem, StartItem, StateItem, TransitionItem}
import infrastructure.toolbox.section.selector.mouse.{DeleteMouseSelector, NormalMouseSelector}
import javafx.event.EventHandler
import javafx.scene.input.{MouseButton, MouseEvent}

class InfrastructureController(drawingPane: DrawingPane, toolBox: ToolBox, propertiesBox: PropertiesBox) {
  private var mouseX: Double = 0.0
  private var mouseY: Double = 0.0

  private var temporalTransition: Option[Transition] = None
  private var ghostElement: Option[GhostElement] = None

  private var actualMenuBar: Option[MenuBar] = None

  private val drawConnectableNodeUseCase = new DrawConnectableNodeUseCase(drawingPane)
  private val dragConnectableNodeUseCase = new DragConnectableNodeUseCase(drawingPane)
  private val eraseConnectableNodeUseCase = new EraseConnectableNodeUseCase(drawingPane)
  private val drawTransitionUseCase = new DrawTransitionUseCase(drawingPane)
  private val dragTransitionUseCase = new DragTransitionUseCase(drawingPane)
  private val eraseTransitionUseCase = new EraseTransitionUseCase(drawingPane)

  val state1 = new State(entryActions = List(new EntryAction("Action 1"), new EntryAction("Action 2")))
  val state2 = new State()

  addState(state1, 0, 0)
  addState(state2, 300, 0)

  drawingPane.setOnMouseClicked(drawingPaneMouseClickedListener)
  drawingPane.setOnMouseMoved(drawingPaneMouseMovedListener)


  private def drawingPaneMouseClickedListener: EventHandler[MouseEvent] = (event: MouseEvent) => {
    updateMousePosition(event)

    if (event.getButton == MouseButton.PRIMARY) {
      toolBox.getSelectedTool match {
        case _: TransitionItem =>
          if (temporalTransition.isDefined) {
            cancelTransitionCreation()
            toolBox.setToolToDefault()
          }
          if (actualMenuBar.isDefined) {
            drawingPane.getChildren.remove(actualMenuBar.get)
            actualMenuBar = None
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

  private def addState(state: State, x: Double, y: Double): Unit = {
    setStateListeners(state)
    drawConnectableNodeUseCase.draw(state.shape, x, y)
  }

  private def addStart(start: Start, x: Double, y: Double): Unit = {
    setStartListeners(start)
    drawConnectableNodeUseCase.draw(start.shape, x, y)
  }

  private def addEnd(end: End, x: Double, y: Double): Unit = {
    setEndListeners(end)
    drawConnectableNodeUseCase.draw(end.shape, x, y)
  }

  private def addTransition(transition: Transition): Unit = {
    drawTransitionUseCase.draw(transition.shape, transition.getSourceShape, transition.getDestinationShape)
  }

  private def addTemporalTransition(source: ConnectableElement, x: Double, y: Double): Unit = {
    ghostElement = Some(new GhostElement())

    drawConnectableNodeUseCase.draw(ghostElement.get.shape, x, y)

    val transition = new Transition(source, ghostElement.get)

    ghostElement.get.addInTransition(transition)

    drawTransitionUseCase.draw(transition.shape, transition.getSourceShape, transition.getDestinationShape)

    temporalTransition = Some(transition)
  }

  private def establishTemporalTransition(destination: ConnectableElement): Unit = {
    val source = temporalTransition.get.source

    val newTransition = new Transition(source, destination)
    source.addOutTransition(newTransition)
    destination.addInTransition(newTransition)

    eraseTransitionUseCase.erase(temporalTransition.get.shape)
    addTransition(newTransition)

    temporalTransition = None
  }

  private def dragTemporalTransition(deltaX: Double, deltaY: Double): Unit = {
    dragConnectableNodeUseCase.drag(ghostElement.get.shape, deltaX, deltaY)
    dragTransitionUseCase.drag(temporalTransition.get.shape, temporalTransition.get.getSourceShape, temporalTransition.get.getDestinationShape)
  }

  private def cancelTransitionCreation(): Unit = {
    eraseTransitionUseCase.erase(temporalTransition.get.shape)
    eraseConnectableNodeUseCase.erase(ghostElement.get.shape)

    temporalTransition = None
    ghostElement = None
  }

  private def updateMousePosition(event: MouseEvent): Unit = {
    mouseX = event.getSceneX
    mouseY = event.getSceneY
  }

  private def calculateDeltaFromMouseEvent(event: MouseEvent): (Double, Double) = {
    (event.getSceneX - mouseX, event.getSceneY - mouseY)
  }

  private def setStateListeners(state: State): Unit = {
    val stateShape = state.shape
    val statePropertiesBox = state.propertiesBox
    val stateContextMenu = state.contextMenu

    stateShape.setOnMouseClicked(event => {
      event.consume()

      event.getButton match {
        case MouseButton.PRIMARY =>
          toolBox.getSelectedTool match {
            case _: NormalMouseSelector =>
              propertiesBox.setContent(statePropertiesBox)

            case _: TransitionItem =>
              if (temporalTransition.isDefined) {
                establishTemporalTransition(state)
                toolBox.setToolToDefault()
              } else {
                val point = stateShape.getLocalToParentTransform.transform(event.getX, event.getY)
                addTemporalTransition(state, point.getX, point.getY)
              }

            case _: DeleteMouseSelector =>
              removeConnectableElement(state, stateShape)
              toolBox.setToolToDefault()

            case _ =>

          }
        case _ =>
      }
    })

    stateShape.setOnMouseDragged(event => {
      event.consume()

      toolBox.getSelectedTool match {
        case _: NormalMouseSelector =>
          val (deltaX, deltaY) = calculateDeltaFromMouseEvent(event)
          dragConnectableNodeUseCase.drag(stateShape, deltaX, deltaY)
          state.getTransitions.foreach(transition => dragTransitionUseCase.drag(transition.shape, transition.getSourceShape, transition.getDestinationShape))

        case _ =>
      }

      updateMousePosition(event)
    })

    stateShape.setOnContextMenuRequested(event => {
      stateContextMenu.show(stateShape, event.getScreenX, event.getScreenY)
    })

    stateContextMenu.getItems.forEach(action => action.setOnAction(event => {

      event.getSource match {
        case _: AddEntryActionMenuItem =>
          state.addEntryAction()
        //TODO: call the model

        case _: AddExitActionMenuItem =>
          state.addExitAction()
        //TODO: call the model

        case _ =>
      }
    }))

    statePropertiesBox.setOnNameEditedListener(event => {
      val newName = statePropertiesBox.getName
      state.shape.setName(newName)
    })
  }

  private def setStartListeners(start: Start): Unit = {
    val startShape = start.shape

    startShape.setOnMouseClicked(event => {
      event.consume()

      event.getButton match {
        case MouseButton.PRIMARY =>
          toolBox.getSelectedTool match {
            case _: TransitionItem =>
              if (temporalTransition.isDefined) {
                establishTemporalTransition(start)
                toolBox.setToolToDefault()
              } else {
                val point = startShape.getLocalToParentTransform.transform(event.getX, event.getY)
                addTemporalTransition(start, point.getX, point.getY)
              }

            case _: DeleteMouseSelector =>
              removeConnectableElement(start, startShape)
              toolBox.setToolToDefault()

            case _ =>
          }

        case _ =>
      }
    })

    startShape.setOnMouseDragged(event => {
      event.consume()

      toolBox.getSelectedTool match {
        case _: NormalMouseSelector =>
          val (deltaX, deltaY) = calculateDeltaFromMouseEvent(event)
          dragConnectableNodeUseCase.drag(startShape, deltaX, deltaY)
          start.getTransitions.foreach(transition => dragTransitionUseCase.drag(transition.shape, transition.getSourceShape, transition.getDestinationShape))

        case _ =>
      }

      updateMousePosition(event)
    })
  }

  private def setEndListeners(end: End): Unit = {
    val endShape = end.shape

    endShape.setOnMouseClicked(event => {
      event.consume()

      event.getButton match {
        case MouseButton.PRIMARY =>
          toolBox.getSelectedTool match {
            case _: TransitionItem =>
              if (temporalTransition.isDefined) {
                establishTemporalTransition(end)
                toolBox.setToolToDefault()
              } else {
                val point = endShape.getLocalToParentTransform.transform(event.getX, event.getY)
                addTemporalTransition(end, point.getX, point.getY)
              }

            case _: DeleteMouseSelector =>
              removeConnectableElement(end, endShape)
              toolBox.setToolToDefault()

            case _ =>

          }

        case _ =>

      }
    })

    endShape.setOnMouseDragged(event => {
      event.consume()

      toolBox.getSelectedTool match {
        case _: NormalMouseSelector =>
          val (deltaX, deltaY) = calculateDeltaFromMouseEvent(event)
          dragConnectableNodeUseCase.drag(endShape, deltaX, deltaY)
          end.getTransitions.foreach(transition => dragTransitionUseCase.drag(transition.shape, transition.getSourceShape, transition.getDestinationShape))

        case _ =>

      }

      updateMousePosition(event)
    })
  }

  private def removeConnectableElement(connectableElement: ConnectableElement, connectableShape: ConnectableShape): Unit = {
    connectableElement.getTransitions.foreach(transition => removeTransition(transition))
    eraseConnectableNodeUseCase.erase(connectableShape)
  }

  private def removeTransition(transition: Transition): Unit = {
    transition.source.outTransitions = transition.source.outTransitions.filterNot(t => t == transition)
    transition.destination.inTransitions = transition.destination.inTransitions.filterNot(t => t == transition)

    eraseTransitionUseCase.erase(transition.shape)
  }
}
