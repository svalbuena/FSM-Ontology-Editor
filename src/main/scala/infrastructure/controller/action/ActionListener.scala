package infrastructure.controller.action

import infrastructure.controller.DrawingPaneController
import infrastructure.drawingpane.DrawingPane
import infrastructure.elements.action.Action
import infrastructure.elements.guard.Guard
import infrastructure.elements.node.State
import infrastructure.id.IdGenerator
import infrastructure.menu.contextmenu.action.item.DeleteActionMenuItem

class ActionListener(action: Action, drawingPaneController: DrawingPaneController, drawingPane: DrawingPane, idGenerator: IdGenerator) {
  private val propertiesBox = action.propertiesBox
  private val contextMenu = action.contextMenu

  private val shape = action.shape

  propertiesBox.setOnActionNameChanged(name => {
    //TODO: notify the model
    action.name = name

    propertiesBox.setTiltedPaneName(name)
    shape.setActionName(name)

    println("Action name changed to -> " + name)
  })

  propertiesBox.setOnAbsoluteUriChanged(absoluteUri => {
    //TODO: notify the model
    action.absoluteUri = absoluteUri

    println("Absolute uri changed to -> " + absoluteUri)
  })

  propertiesBox.setOnUriTypeChanged(uriType => {
    //TODO: notify the model
    action.uriType = uriType

    propertiesBox.setUriType(uriType)

    println("Uri type changed to -> " + uriType)
  })

  propertiesBox.setOnRemoveActionButtonClicked(() => {
    //TODO: notify the model
    removeAction()

    println("Removing an action")
  })

  shape.setOnContextMenuRequested(event => {
    event.consume()

    contextMenu.show(shape, event.getScreenX, event.getScreenY)
  })

  contextMenu.getItems.forEach {
    case menuItem: DeleteActionMenuItem =>
      menuItem.setOnAction(event => {
        //TODO: notify the model
        println("Removing an action")
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

          drawingPaneController.removeActionFromState(action, state)

        case guard: Guard =>
          guard.actions = guard.actions.filterNot(a => a == action)

          drawingPaneController.removeActionFromGuard(action, guard)

        case _ =>
      }
    }

  }
}
