package infrastructure.controller.node

import infrastructure.controller.InfrastructureController
import infrastructure.drawingpane.DrawingPane
import infrastructure.elements.action.ActionType.ActionType
import infrastructure.elements.action.{Action, ActionType}
import infrastructure.elements.node.State
import infrastructure.id.IdGenerator
import infrastructure.menu.contextmenu.state.item.{AddEntryActionMenuItem, AddExitActionMenuItem}
import infrastructure.toolbox.section.item.fsm.TransitionItem
import infrastructure.toolbox.section.selector.mouse.{DeleteMouseSelector, NormalMouseSelector}
import javafx.scene.input.MouseButton

class StateListener(state: State, infrastructureController: InfrastructureController, drawingPane: DrawingPane, idGenerator: IdGenerator) {
  private val toolBox = infrastructureController.toolBox
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
            infrastructureController.propertiesBox.setContent(propertiesBox)

          case _: TransitionItem =>
            if (infrastructureController.isTemporalTransitionDefined) {
              infrastructureController.establishTemporalTransition(state)
              toolBox.setToolToDefault()
            } else {
              val point = shape.getLocalToParentTransform.transform(event.getX, event.getY)
              infrastructureController.addTemporalTransition(state, point.getX, point.getY)
            }

          case _: DeleteMouseSelector =>
            infrastructureController.removeConnectableElement(state, shape)
            infrastructureController.propertiesBox.removeContent()
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
        val (deltaX, deltaY) = infrastructureController.calculateDeltaFromMouseEvent(event)
        canvas.dragConnectableNode(shape, deltaX, deltaY)
        state.getTransitions.foreach(transition => canvas.dragTransition(transition.shape, transition.getSourceShape, transition.getDestinationShape))

      case _ =>
    }

    infrastructureController.updateMousePosition(event)
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
    state.shape.setName(name)
  })

  propertiesBox.setOnAddEntryActionButtonClicked(() => {
    addAction(ActionType.ENTRY)
  })

  propertiesBox.setOnAddExitActionButtonClicked(() => {
    addAction(ActionType.EXIT)
  })

  private def addAction(actionType: ActionType): Unit = {
    var actionList = actionType match {
      case infrastructure.elements.action.ActionType.ENTRY => state.entryActions
      case infrastructure.elements.action.ActionType.EXIT => state.exitActions
    }

    val id = idGenerator.getId
    val action = new Action(id, actionType, "Action" + id)

    actionList = action :: actionList

    infrastructureController.addActionToState(action, state)
  }
}
