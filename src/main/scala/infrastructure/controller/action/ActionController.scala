package infrastructure.controller.action

import application.command.action.add.{AddActionToGuardCommand, AddActionToStateCommand}
import application.command.action.modify._
import application.command.action.remove.{RemoveActionFromGuardCommand, RemoveActionFromStateCommand}
import application.commandhandler.action.add.{AddActionToGuardHandler, AddActionToStateHandler}
import application.commandhandler.action.modify._
import application.commandhandler.action.remove.{RemoveActionFromGuardHandler, RemoveActionFromStateHandler}
import infrastructure.controller.DrawingPaneController
import infrastructure.drawingpane.DrawingPane
import infrastructure.element.action.ActionType.ActionType
import infrastructure.element.action.MethodType.MethodType
import infrastructure.element.action.UriType.UriType
import infrastructure.element.action.{Action, ActionType}
import infrastructure.element.body.Body
import infrastructure.element.guard.Guard
import infrastructure.element.prototypeuri.PrototypeUri
import infrastructure.element.state.State
import infrastructure.id.IdGenerator
import infrastructure.menu.contextmenu.action.item.DeleteActionMenuItem

class ActionController(action: Action, drawingPaneController: DrawingPaneController, drawingPane: DrawingPane, idGenerator: IdGenerator) {
  private val propertiesBox = action.propertiesBox
  private val contextMenu = action.contextMenu

  private val shape = action.shape

  propertiesBox.setOnAbsoluteUriChanged(newAbsoluteUri => ActionController.modifyActionAbsoluteUri(action, newAbsoluteUri))
  propertiesBox.setOnMethodTypeChanged(newMethodType => ActionController.modifyActionMethodType(action, newMethodType))
  propertiesBox.setOnActionNameChanged(newName => ActionController.modifyActionName(action, newName))
  propertiesBox.setOnTimeoutChanged(newTimeout => ActionController.modifyActionTimeout(action, newTimeout))
  //propertiesBox.setOnActionTypeChanged(newActionType => ActionController.modifyActionType(action, newActionType))
  propertiesBox.setOnUriTypeChanged(newUriType => ActionController.modifyActionUriType(action, newUriType))

  propertiesBox.setOnRemoveActionButtonClicked(() => removeAction())

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
        case guard: Guard => ActionController.removeActionFromGuard(action, guard, drawingPaneController)
        case state: State => ActionController.removeActionFromState(action, state, drawingPaneController)
        case _ =>
      }
    }
  }
}

object ActionController {
  def addActionToGuard(guard: Guard, drawingPaneController: DrawingPaneController): Unit = {
    new AddActionToGuardHandler().execute(new AddActionToGuardCommand(guard.name)) match {
      case Left(error) => println(error.getMessage)
      case Right(names) =>
        val (actionName, bodyName, prototypeUriName) = (names._1, names._2, names._3)
        val action = new Action(actionName, ActionType.GUARD, body = new Body(bodyName), prototypeUri = new PrototypeUri(prototypeUriName))

        guard.actions = action :: guard.actions

        drawingPaneController.addActionToGuard(action, guard)

        println("Adding guard action to a guard")
    }
  }

  def addActionToState(actionType: ActionType, state: State, drawingPaneController: DrawingPaneController): Unit = {
    new AddActionToStateHandler().execute(new AddActionToStateCommand(actionType, state.name)) match {
      case Left(error) => println(error.getMessage)
      case Right(names) =>
        val (actionName, bodyName, prototypeUriName) = (names._1, names._2, names._3)
        val action = new Action(actionName, actionType, body = new Body(bodyName), prototypeUri = new PrototypeUri(prototypeUriName))

        state.actions = action :: state.actions

        drawingPaneController.addActionToState(action, state)

        println(s"Adding $actionType action to a state")
    }
  }

  def modifyActionAbsoluteUri(action: Action, newAbsoluteUri: String): Unit = {
    new ModifyActionAbsoluteUriHandler().execute(new ModifyActionAbsoluteUriCommand(action.name, newAbsoluteUri)) match {
      case Left(error) => println(error.getMessage)
      case Right(_) =>
        action.absoluteUri = newAbsoluteUri

        println("Absolute uri changed to -> " + newAbsoluteUri)
    }
  }

  def modifyActionMethodType(action: Action, newMethodType: MethodType): Unit = {
    new ModifyActionMethodTypeHandler().execute(new ModifyActionMethodTypeCommand(action.name, newMethodType match {
      case infrastructure.element.action.MethodType.GET => application.command.action.modify.MethodType.GET
      case infrastructure.element.action.MethodType.POST => application.command.action.modify.MethodType.POST
    })) match {
      case Left(error) => println(error.getMessage)
      case Right(_) =>
        action.method = newMethodType

        println("Method type changed to -> " + newMethodType)
    }
  }

  def modifyActionName(action: Action, newName: String): Unit = {
    new ModifyActionNameHandler().execute(new ModifyActionNameCommand(action.name, newName)) match {
      case Left(error) => println(error.getMessage)
      case Right(_) =>
        action.name = newName

        action.propertiesBox.setTiltedPaneName(newName)
        action.shape.setActionName(newName)

        println("Action name changed to -> " + newName)
    }
  }

  def modifyActionTimeout(action: Action, newTimeout: Int): Unit = {
    new ModifyActionTimeoutHandler().execute(new ModifyActionTimeoutCommand(action.name, newTimeout)) match {
      case Left(error) => println(error.getMessage)
      case Right(_) =>
        action.timeout = newTimeout

        println("Timeout changed to -> " + newTimeout)
    }
  }

  def modifyActionType(action: Action, newActionType: ActionType): Unit = {
    new ModifyActionTypeHandler().execute(new ModifyActionTypeCommand(action.name, newActionType match {
      case infrastructure.element.action.ActionType.ENTRY => application.command.action.modify.ActionType.ENTRY
      case infrastructure.element.action.ActionType.EXIT => application.command.action.modify.ActionType.EXIT
      case infrastructure.element.action.ActionType.GUARD => application.command.action.modify.ActionType.GUARD
    })) match {
      case Left(error) => println(error.getMessage)
      case Right(_) =>
        action.actionType = newActionType

        println("Timeout changed to -> " + newActionType)
    }
  }

  def modifyActionUriType(action: Action, newUriType: UriType): Unit = {
    new ModifyActionUriTypeHandler().execute(new ModifyActionUriTypeCommand(action.name, newUriType match {
      case infrastructure.element.action.UriType.ABSOLUTE => application.command.action.modify.UriType.ABSOLUTE
      case infrastructure.element.action.UriType.PROTOTYPE => application.command.action.modify.UriType.PROTOTYPE
    })) match {
      case Left(error) => println(error.getMessage)
      case Right(_) =>
        action.uriType = newUriType

        action.propertiesBox.setUriType(newUriType)

        println("Uri type changed to -> " + newUriType)
    }
  }

  def removeActionFromGuard(action: Action, guard: Guard, drawingPaneController: DrawingPaneController): Unit = {
    new RemoveActionFromGuardHandler().execute(new RemoveActionFromGuardCommand(action.name, guard.name)) match {
      case Left(error) => println(error.getMessage)
      case Right(_) =>
        guard.actions = guard.actions.filterNot(a => a == action)

        drawingPaneController.removeActionFromGuard(action, guard)
    }
  }

  def removeActionFromState(action: Action, state: State, drawingPaneController: DrawingPaneController): Unit = {
    new RemoveActionFromStateHandler().execute(new RemoveActionFromStateCommand(action.name, state.name)) match {
      case Left(error) => println(error.getMessage)
      case Right(_) =>
        state.actions = state.actions.filterNot(a => a == action)

        drawingPaneController.removeActionFromState(action, state)
    }
  }
}
