package infrastructure.controller.action

import infrastructure.controller.DrawingPaneController
import infrastructure.drawingpane.DrawingPane
import infrastructure.elements.action.Action
import infrastructure.elements.guard.Guard
import infrastructure.elements.state.State
import infrastructure.id.IdGenerator
import infrastructure.menu.contextmenu.action.item.DeleteActionMenuItem

class ActionController(action: Action, drawingPaneController: DrawingPaneController, drawingPane: DrawingPane, idGenerator: IdGenerator) {
  private val propertiesBox = action.propertiesBox
  private val contextMenu = action.contextMenu

  private val shape = action.shape

  propertiesBox.setOnActionNameChanged(name => {
    //TODO: notify the model, ModifyActionName
    action.name = name

    propertiesBox.setTiltedPaneName(name)
    shape.setActionName(name)

    println("Action name changed to -> " + name)
  })

  propertiesBox.setOnTimeoutChanged(timeout => {
    //TODO: notify hte model, ModifyActionTimeout
    action.timeout = timeout

    println("Timeout changed to -> " + timeout)
  })

  propertiesBox.setOnMethodTypeChanged(methodType => {
    //TODO: notify the model, ModifyActionMethodType
    action.method = methodType

    println("Method type changed to -> " + methodType)
  })

  propertiesBox.setOnAbsoluteUriChanged(absoluteUri => {
    //TODO: notify the model, ModifyActionAbsoluteUri
    action.absoluteUri = absoluteUri

    println("Absolute uri changed to -> " + absoluteUri)
  })

  propertiesBox.setOnUriTypeChanged(uriType => {
    //TODO: notify the model, ModifyActionUriType
    action.uriType = uriType

    propertiesBox.setUriType(uriType)

    println("Uri type changed to -> " + uriType)
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
    println("Removing an action")
    if (action.hasParent) {
      action.getParent match {
        case state: State =>
          //TODO: notify the model, RemoveActionFromState
          state.actions = state.actions.filterNot(a => a == action)

          drawingPaneController.removeActionFromState(action, state)

        case guard: Guard =>
          //TODO: notify the model, RemoveActionFromGuard
          guard.actions = guard.actions.filterNot(a => a == action)

          drawingPaneController.removeActionFromGuard(action, guard)

        case _ =>
      }
    }
  }
}

object ActionController {
  def addActionToGuard(): Unit = {
  }

  def addActionToState(): Unit = {

  }

  def modifyActionAbsoluteUri(): Unit = {

  }

  def modifyActionMethod(): Unit = {

  }

  def modifyActionName(): Unit = {

  }

  def modifyActionTimeout(): Unit = {

  }

  def modifyActionType(): Unit = {

  }

  def modifyActionUriType(): Unit = {

  }

  def removeActionFromGuard(): Unit = {

  }

  def removeActionFromState(): Unit = {

  }
}
