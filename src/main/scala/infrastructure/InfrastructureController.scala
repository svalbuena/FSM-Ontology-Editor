package infrastructure

import java.awt.MenuBar

import infrastructure.drawingpane.DrawingPane
import infrastructure.drawingpane.shape._
import infrastructure.drawingpane.shape.state.StateShape
import infrastructure.drawingpane.usecase.connectablenode._
import infrastructure.drawingpane.usecase.transition._
import infrastructure.elements.action.EntryAction
import infrastructure.elements.state.State
import infrastructure.menu.contextmenu.state.StateContextMenu
import infrastructure.menu.contextmenu.state.item.{AddEntryActionMenuItem, AddExitActionMenuItem}
import infrastructure.propertybox.PropertiesBox
import infrastructure.propertybox.state.StatePropertiesBox
import infrastructure.toolbox.ToolBox
import infrastructure.toolbox.section.item.fsm.{EndItem, StartItem, StateItem, TransitionItem}
import infrastructure.toolbox.section.selector.mouse.{DeleteMouseSelector, NormalMouseSelector}
import javafx.event.EventHandler
import javafx.scene.input.{MouseButton, MouseEvent}

class InfrastructureController(drawingPane: DrawingPane, toolBox: ToolBox, propertiesBox: PropertiesBox) {
  private var mouseX: Double = 0.0
  private var mouseY: Double = 0.0

  private var temporalTransition: Option[Transition] = None

  private var actualMenuBar: Option[MenuBar] = None

  private val drawConnectableNodeUseCase = new DrawConnectableNodeUseCase(drawingPane)
  private val dragConnectableNodeUseCase = new DragConnectableNodeUseCase(drawingPane)
  private val eraseConnectableNodeUseCase = new EraseConnectableNodeUseCase(drawingPane)
  private val drawTransitionUseCase = new DrawTransitionUseCase(drawingPane)
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

    drawConnectableNodeUseCase.draw(start, x, y)
  }

  private def addEnd(end: End, x: Double, y: Double): Unit = {

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

  private def setStateListeners(state: State): Unit = {
    val stateShape = state.shape
    val statePropertiesBox = state.propertiesBox
    val stateContextMenu = state.contextMenu

    stateShape.setOnMouseClicked(event => {
      event.getButton match {
        case MouseButton.PRIMARY =>
          toolBox.getSelectedTool match {
            case _: NormalMouseSelector =>
              propertiesBox.setContent(statePropertiesBox)

            case _: TransitionItem =>
              if (temporalTransition.isDefined) {
                establishTemporalTransition(stateShape)
                toolBox.setToolToDefault()
              } else {
                val point = stateShape.getLocalToParentTransform.transform(event.getX, event.getY)
                addTemporalTransition(stateShape, point.getX, point.getY)
              }

            case _: DeleteMouseSelector =>
              eraseConnectableNodeUseCase.erase(stateShape)
              toolBox.setToolToDefault()
          }
      }
    })

    stateShape.setOnMouseDragged(event => {
      event.consume()

      toolBox.getSelectedTool match {
        case _: NormalMouseSelector =>
          val (deltaX, deltaY) = calculateDeltaFromMouseEvent(event)
          dragConnectableNodeUseCase.drag(stateShape, deltaX, deltaY)
      }

      updateMousePosition(event)
    })

    stateShape.setOnContextMenuRequested(event => {
      stateContextMenu.show(stateShape, event.getScreenX, event.getScreenY)
    })

    stateContextMenu.getItems.forEach(action => action.setOnAction(event => {
      event.getSource match {
        case _: AddEntryActionMenuItem =>
        //TODO: call the model

        case _: AddExitActionMenuItem =>
        //TODO: call the model

        case _ =>
      }
    }))

    statePropertiesBox.setOnNameEditedListener(event => {
      val newName = statePropertiesBox.getName
      state.setName(newName)
    })
  }

  private def setStartListeners(startShape: Start): Unit = {
    startShape.setOnMouseClicked(event => {
      event.getButton match {
        case MouseButton.PRIMARY =>
          toolBox.getSelectedTool match {
            case _: TransitionItem =>
              if (temporalTransition.isDefined) {
                establishTemporalTransition(startShape)
                toolBox.setToolToDefault()
              } else {
                val point = startShape.getLocalToParentTransform.transform(event.getX, event.getY)
                addTemporalTransition(startShape, point.getX, point.getY)
              }

            case _: DeleteMouseSelector =>
              eraseConnectableNodeUseCase.erase(startShape)
              toolBox.setToolToDefault()
          }
      }
    })

    startShape.setOnMouseDragged(event => {
      event.consume()

      toolBox.getSelectedTool match {
        case _: NormalMouseSelector =>
          val (deltaX, deltaY) = calculateDeltaFromMouseEvent(event)
          dragConnectableNodeUseCase.drag(startShape, deltaX, deltaY)
      }

      updateMousePosition(event)
    })


  }

  private def setEndListeners(endShape: End): Unit = {
    endShape.setOnMouseClicked(event => {
      event.getButton match {
        case MouseButton.PRIMARY =>
          toolBox.getSelectedTool match {
            case _: TransitionItem =>
              if (temporalTransition.isDefined) {
                establishTemporalTransition(endShape)
                toolBox.setToolToDefault()
              } else {
                val point = endShape.getLocalToParentTransform.transform(event.getX, event.getY)
                addTemporalTransition(endShape, point.getX, point.getY)
              }

            case _: DeleteMouseSelector =>
              eraseConnectableNodeUseCase.erase(endShape)
              toolBox.setToolToDefault()
          }
      }
    })

    endShape.setOnMouseDragged(event => {
      event.consume()

      toolBox.getSelectedTool match {
        case _: NormalMouseSelector =>
          val (deltaX, deltaY) = calculateDeltaFromMouseEvent(event)
          dragConnectableNodeUseCase.drag(endShape, deltaX, deltaY)
      }

      updateMousePosition(event)
    })
  }
}
