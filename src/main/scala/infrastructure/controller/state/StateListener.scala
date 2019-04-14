package infrastructure.controller.state

import infrastructure.controller.DrawingPaneController
import infrastructure.drawingpane.DrawingPane
import infrastructure.elements.action.ActionType.ActionType
import infrastructure.elements.action.{Action, ActionType}
import infrastructure.elements.node.State
import infrastructure.id.IdGenerator
import infrastructure.menu.contextmenu.state.item.{AddEntryActionMenuItem, AddExitActionMenuItem}
import infrastructure.toolbox.section.item.fsm.TransitionItem
import infrastructure.toolbox.section.selector.mouse.{DeleteMouseSelector, NormalMouseSelector}
import javafx.scene.input.MouseButton

class StateListener(state: State, drawingPaneController: DrawingPaneController, drawingPane: DrawingPane, idGenerator: IdGenerator) {
  private val toolBox = drawingPaneController.toolBox
  private val canvas = drawingPane.canvas

  private val shape = state.shape
  private val propertiesBox = state.propertiesBox
  private val contextMenu = state.contextMenu

  shape.setOnMouseClicked(event => {
    event.consume()

    event.getButton match {
      case MouseButton.PRIMARY =>
        toolBox.getSelectedTool match {
          case _: NormalMouseSelector =>
            drawingPaneController.propertiesBox.setContent(propertiesBox)

          case _: TransitionItem =>
            if (drawingPaneController.isTemporalTransitionDefined) {
              drawingPaneController.establishTemporalTransition(state)
              toolBox.setToolToDefault()
            } else {
              val point = shape.getLocalToParentTransform.transform(event.getX, event.getY)
              drawingPaneController.addTemporalTransition(state, point.getX, point.getY)
            }

          case _: DeleteMouseSelector =>
            //TODO: notify the model
            println("Removing a state")
            drawingPaneController.removeConnectableElement(state, shape)
            drawingPaneController.propertiesBox.removeContent()
            toolBox.setToolToDefault()

          case _ =>

        }
      case _ =>
    }
  })

  shape.setOnMouseDragged(event => {
    event.consume()

    toolBox.getSelectedTool match {
      case _: NormalMouseSelector =>
        val (deltaX, deltaY) = drawingPaneController.calculateDeltaFromMouseEvent(event)
        canvas.dragConnectableNode(shape, deltaX, deltaY)
        state.getTransitions.foreach(transition => canvas.dragTransition(transition.shape, transition.getSourceShape, transition.getDestinationShape))

      case _ =>
    }

    drawingPaneController.updateMousePosition(event)
  })

  shape.setOnContextMenuRequested(event => {
    event.consume()

    contextMenu.show(shape, event.getScreenX, event.getScreenY)
  })

  contextMenu.getItems.forEach {
    case menuItem: AddEntryActionMenuItem =>
      menuItem.setOnAction(event => {
        //TODO: notify the model
        addAction(ActionType.ENTRY)
      })

    case menuItem: AddExitActionMenuItem =>
      menuItem.setOnAction(event => {
        //TODO: notify the model
        addAction(ActionType.EXIT)
      })
  }

  propertiesBox.setOnStateNameChanged(name => {
    //TODO: notify the model
    state.shape.setName(name)

    println("State name changed to -> " + name)
  })

  propertiesBox.setOnAddEntryActionButtonClicked(() => {
    //TODO: notify the model
    addAction(ActionType.ENTRY)

    println("Adding entry action to a state")
  })

  propertiesBox.setOnAddExitActionButtonClicked(() => {
    //TODO: notify the model
    addAction(ActionType.EXIT)

    println("Adding exit action to a state")
  })

  private def addAction(actionType: ActionType): Unit = {
    val id = idGenerator.getId
    val action = new Action(id, actionType, "Action" + id)

    state.actions = action :: state.actions

    drawingPaneController.addActionToState(action, state)
  }
}
