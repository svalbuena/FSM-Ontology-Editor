package application.commandhandler.action.add

import application.command.action.add.AddActionToStateCommand
import application.commandhandler.ActionTypeError
import domain.Environment
import domain.action.{Action, ActionType}

class AddActionToStateHandler {

  /**
    *
    * @param addActionToStateCommand command
    * @return returns an exception or the name of the action, the name of the body action and the name of the prototype uri action
    */
  def execute(addActionToStateCommand: AddActionToStateCommand): Either[Exception, (String, String, String)] = {
    Environment.getState(addActionToStateCommand.stateName) match {
      case Left(error) => Left(error)
      case Right(state) =>
        val actionType = addActionToStateCommand.actionType match {
          case infrastructure.element.action.ActionType.ENTRY => ActionType.ENTRY
          case infrastructure.element.action.ActionType.EXIT => ActionType.EXIT
          case infrastructure.element.action.ActionType.GUARD => ActionType.GUARD
        }
        if (actionType == ActionType.GUARD) Left(new ActionTypeError("An Action for a State can't be of Guard type"))

        val action = new Action(actionType)
        state.addAction(action) match {
          case Left(error) => Left(error)
          case Right(_) => Right(action.name, action.body.name, action.prototypeUri.name)
        }
    }
  }
}
