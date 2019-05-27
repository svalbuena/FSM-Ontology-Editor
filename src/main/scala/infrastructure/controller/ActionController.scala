package infrastructure.controller

import application.command.action.add.{AddActionToGuardCommand, AddActionToStateCommand}
import application.command.action.modify._
import application.command.action.remove.{RemoveActionFromGuardCommand, RemoveActionFromStateCommand}
import application.commandhandler.action.add.{AddActionToGuardHandler, AddActionToStateHandler}
import application.commandhandler.action.modify._
import application.commandhandler.action.remove.{RemoveActionFromGuardHandler, RemoveActionFromStateHandler}
import infrastructure.element.action.ActionType.ActionType
import infrastructure.element.action.MethodType.MethodType
import infrastructure.element.action.UriType.UriType
import infrastructure.element.action.{Action, ActionType}
import infrastructure.element.body.Body
import infrastructure.element.guard.Guard
import infrastructure.element.prototypeuri.PrototypeUri
import infrastructure.element.state.State
import infrastructure.main.EnvironmentSingleton
import infrastructure.menu.contextmenu.action.item.DeleteActionMenuItem

/**
  * Controls the visual and behavior aspects of an Action
  *
  * @param action action to control
  */
class ActionController(action: Action) {
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
      menuItem.setOnAction(_ => {
        removeAction()
      })
    case _ =>
  }

  private def removeAction(): Unit = {
    action.parent match {
      case guard: Guard => ActionController.removeActionFromGuard(action, guard)
      case state: State => ActionController.removeActionFromState(action, state)
      case _ =>
    }
  }
}

/**
  * Operations that can be done with an action
  */
object ActionController {
  private val environment = EnvironmentSingleton.get()

  /**
    * Creates an action and adds it to a guard
    *
    * @param guard guard where the action will be added
    * @return the created action if there were no errors
    */
  def addActionToGuard(guard: Guard): Option[Action] = {
    new AddActionToGuardHandler(environment).execute(new AddActionToGuardCommand(guard.name)) match {
      case Left(error) =>
        println(error.getMessage)
        None
      case Right(names) =>
        val (actionName, absoluteUri, bodyName, prototypeUriName) = (names._1, names._2, names._3, names._4)
        val action = new Action(actionName, ActionType.GUARD, absoluteUri = absoluteUri, body = new Body(bodyName), prototypeUri = new PrototypeUri(prototypeUriName), parent = guard)

        guard.addAction(action)

        drawAction(action)

        println("Adding guard action to a guard")

        Some(action)
    }
  }

  /**
    * Draw an action on the application
    *
    * @param action action to be drawn
    */
  def drawAction(action: Action): Unit = {
    action.propertiesBox.setActionType(action.actionType)
    action.propertiesBox.setActionName(action.name)
    action.propertiesBox.setMethodType(action.method)
    action.propertiesBox.setUriType(action.uriType)
    action.propertiesBox.setTimeout(action.timeout)
    action.propertiesBox.setAbsoluteUri(action.absoluteUri)

    action.shape.setActionType(action.actionType)
    action.shape.setActionName(action.name)

    BodyController.drawBody(action.body)
    PrototypeUriController.drawPrototypeUri(action.prototypeUri)

    action.parent match {
      case guard: Guard => guard.parent.shape.updateGuardGroupPosition()
      case _ =>
    }

    new ActionController(action)
  }

  /**
    * Creates an action and adds it to a state
    *
    * @param actionType            action type of the new action
    * @param state                 state where the action will be added
    * @param drawingPaneController controller of the drawing pane
    * @return the created action if there were no errors
    */
  def addActionToState(actionType: ActionType, state: State, drawingPaneController: DrawingPaneController): Option[Action] = {
    val infActionType = actionType match {
      case infrastructure.element.action.ActionType.ENTRY => application.command.action.modify.ActionType.ENTRY
      case infrastructure.element.action.ActionType.EXIT => application.command.action.modify.ActionType.EXIT
      case infrastructure.element.action.ActionType.GUARD => application.command.action.modify.ActionType.GUARD
    }

    new AddActionToStateHandler(environment).execute(new AddActionToStateCommand(infActionType, state.name)) match {
      case Left(error) =>
        println(error.getMessage)
        None
      case Right(names) =>
        val (actionName, absoluteUri, bodyName, prototypeUriName) = (names._1, names._2, names._3, names._4)
        val action = new Action(actionName, actionType, absoluteUri = absoluteUri, body = new Body(bodyName), prototypeUri = new PrototypeUri(prototypeUriName), parent = state)

        state.addAction(action)

        drawAction(action)

        println(s"Adding $actionType action to a state")
        Some(action)
    }
  }

  /**
    * Modifies the absolute uri of an action
    *
    * @param action         action to be modified
    * @param newAbsoluteUri new absolute uri
    */
  def modifyActionAbsoluteUri(action: Action, newAbsoluteUri: String): Unit = {
    new ModifyActionAbsoluteUriHandler(environment).execute(new ModifyActionAbsoluteUriCommand(action.name, newAbsoluteUri)) match {
      case Left(error) => println(error.getMessage)
      case Right(_) =>
        action.absoluteUri = newAbsoluteUri

        println("Absolute uri changed to -> " + newAbsoluteUri)
    }
  }

  /**
    * Modifies the method type of an action
    *
    * @param action        action to be modified
    * @param newMethodType new method type
    */
  def modifyActionMethodType(action: Action, newMethodType: MethodType): Unit = {
    new ModifyActionMethodTypeHandler(environment).execute(new ModifyActionMethodTypeCommand(action.name, newMethodType match {
      case infrastructure.element.action.MethodType.GET => application.command.action.modify.MethodType.GET
      case infrastructure.element.action.MethodType.POST => application.command.action.modify.MethodType.POST
    })) match {
      case Left(error) => println(error.getMessage)
      case Right(_) =>
        action.method = newMethodType

        println("Method type changed to -> " + newMethodType)
    }
  }

  /**
    * Modifies the name of an action
    *
    * @param action  action to be modified
    * @param newName new name
    */
  def modifyActionName(action: Action, newName: String): Unit = {
    new ModifyActionNameHandler(environment).execute(new ModifyActionNameCommand(action.name, newName)) match {
      case Left(error) => println(error.getMessage)
      case Right(_) =>
        action.name = newName

        action.shape.setActionName(newName)

        action.parent match {
          case state: State =>
            state.propertiesBox.setActionPropertiesBoxTitle(action.propertiesBox, action.actionType, action.name)

          case guard: Guard =>
            guard.propertiesBox.setActionPropertiesBoxTitle(action.propertiesBox, action.name)

          case _ =>
        }

        println("Action name changed to -> " + newName)
    }
  }

  /**
    * Modifies the timeout of an action
    *
    * @param action     action to be modified
    * @param newTimeout new timeout
    */
  def modifyActionTimeout(action: Action, newTimeout: String): Unit = {
    new ModifyActionTimeoutHandler(environment).execute(new ModifyActionTimeoutCommand(action.name, newTimeout)) match {
      case Left(error) => println(error.getMessage)
      case Right(_) =>
        action.timeout = newTimeout

        println("Timeout changed to -> " + newTimeout)
    }
  }

  /**
    * Modifies the type of an action
    *
    * @param action        action to be modified
    * @param newActionType new action type
    */
  def modifyActionType(action: Action, newActionType: ActionType): Unit = {
    new ModifyActionTypeHandler(environment).execute(new ModifyActionTypeCommand(action.name, newActionType match {
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

  /**
    * Modifies the uri type of an action
    *
    * @param action     action to be modified
    * @param newUriType new uri type
    */
  def modifyActionUriType(action: Action, newUriType: UriType): Unit = {
    new ModifyActionUriTypeHandler(environment).execute(new ModifyActionUriTypeCommand(action.name, newUriType match {
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

  /**
    * Removes an action from a guard
    *
    * @param action action to be removed
    * @param guard  guard where the action belongs
    */
  def removeActionFromGuard(action: Action, guard: Guard): Unit = {
    new RemoveActionFromGuardHandler(environment).execute(new RemoveActionFromGuardCommand(action.name, guard.name)) match {
      case Left(error) => println(error.getMessage)
      case Right(_) => guard.removeAction(action)
    }
  }

  /**
    * Removes an action from a state
    *
    * @param action action to be removed
    * @param state  state where the action belongs
    */
  def removeActionFromState(action: Action, state: State): Unit = {
    new RemoveActionFromStateHandler(environment).execute(new RemoveActionFromStateCommand(action.name, state.name)) match {
      case Left(error) => println(error.getMessage)
      case Right(_) => state.removeAction(action)
    }
  }
}
