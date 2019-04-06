package infrastructure.controller.action

import infrastructure.controller.InfrastructureController
import infrastructure.drawingpane.DrawingPane
import infrastructure.elements.action.Action
import infrastructure.elements.node.State
import infrastructure.id.IdGenerator
import infrastructure.menu.contextmenu.action.item.DeleteActionMenuItem

class ActionListener(action: Action, infrastructureController: InfrastructureController, drawingPane: DrawingPane, idGenerator: IdGenerator) {
  private val propertiesBox = action.propertiesBox
  private val contextMenu = action.contextMenu

  private val shape = action.stateActionPane

  propertiesBox.setOnActionNameChanged(name => {
    //TODO: notify the model
    action.name = name

    propertiesBox.setTiltedPaneName(name)
    shape.setActionName(name)
  })

  propertiesBox.setOnAbsoluteUriChanged(absoluteUri => {
    //TODO: notify the model
    action.absoluteUri = absoluteUri
  })

  propertiesBox.setOnUriTypeChanged(uriType => {
    //TODO: notify the model
    action.uriType = uriType

    propertiesBox.setUriType(uriType)
  })

  propertiesBox.setOnRemoveActionButtonClicked(() => {
    removeAction()
  })

  shape.setOnContextMenuRequested(event => {
    event.consume()

    contextMenu.show(shape, event.getScreenX, event.getScreenY)
  })

  contextMenu.getItems.forEach {
    case menuItem: DeleteActionMenuItem =>
      menuItem.setOnAction(event => {
        removeAction()
      })
    case _ =>
  }

  private def removeAction(): Unit = {
    if (action.hasParent) {
      action.getParent match {
        case state: State =>
          var actionList = action.actionType match {
            case infrastructure.elements.action.ActionType.ENTRY => state.entryActions
            case infrastructure.elements.action.ActionType.EXIT => state.exitActions
          }

          actionList = actionList.filterNot(a => a == action)

          infrastructureController.removeActionFromState(action, state)

        case _ =>
      }
    }

  }
}
