package infrastructure.controller.node

import infrastructure.controller.InfrastructureController
import infrastructure.drawingpane.DrawingPane
import infrastructure.elements.action.{Action, ActionType}
import infrastructure.elements.node.State
import infrastructure.id.IdGenerator
import infrastructure.menu.contextmenu.state.item.{AddEntryActionMenuItem, AddExitActionMenuItem}
import infrastructure.toolbox.section.item.fsm.TransitionItem
import infrastructure.toolbox.section.selector.mouse.{DeleteMouseSelector, NormalMouseSelector}
import javafx.scene.input.MouseButton

class StateListener(state: State, infrastructureController: InfrastructureController, drawingPane: DrawingPane, idGenerator: IdGenerator) {
  private val toolBox = infrastructureController.toolBox
  private val propertiesBox = infrastructureController.propertiesBox
  private val canvas = drawingPane.canvas

  private val stateShape = state.shape
  private val statePropertiesBox = state.propertiesBox
  private val stateContextMenu = state.contextMenu

  stateShape.setOnMouseClicked(event => {
    event.consume()

    event.getButton match {
      case MouseButton.PRIMARY =>
        toolBox.getSelectedTool match {
          case _: NormalMouseSelector =>
            propertiesBox.setContent(statePropertiesBox)

          case _: TransitionItem =>
            if (infrastructureController.isTemporalTransitionDefined) {
              infrastructureController.establishTemporalTransition(state)
              toolBox.setToolToDefault()
            } else {
              val point = stateShape.getLocalToParentTransform.transform(event.getX, event.getY)
              infrastructureController.addTemporalTransition(state, point.getX, point.getY)
            }

          case _: DeleteMouseSelector =>
            infrastructureController.removeConnectableElement(state, stateShape)
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
        val (deltaX, deltaY) = infrastructureController.calculateDeltaFromMouseEvent(event)
        canvas.dragConnectableNode(stateShape, deltaX, deltaY)
        state.getTransitions.foreach(transition => canvas.dragTransition(transition.shape, transition.getSourceShape, transition.getDestinationShape))

      case _ =>
    }

    infrastructureController.updateMousePosition(event)
  })

  stateShape.setOnContextMenuRequested(event => {
    stateContextMenu.show(stateShape, event.getScreenX, event.getScreenY)
  })

  stateContextMenu.getItems.forEach(action => action.setOnAction(event => {
    event.getSource match {
      case _: AddEntryActionMenuItem =>
        //TODO: notify the model
        val entryAction = new Action(idGenerator.getId, ActionType.ENTRY, "Action")
        state.entryActions = entryAction :: state.entryActions

        infrastructureController.addActionState(entryAction, state)

      case _: AddExitActionMenuItem =>
        //TODO: notify the model
        val exitAction = new Action(idGenerator.getId, ActionType.EXIT, "Action")
        state.exitActions = exitAction :: state.exitActions

        infrastructureController.addActionState(exitAction, state)

      case _ =>
    }
  }))

  statePropertiesBox.setOnStateNameChanged(name => {
    state.shape.setName(name)
  })
}
