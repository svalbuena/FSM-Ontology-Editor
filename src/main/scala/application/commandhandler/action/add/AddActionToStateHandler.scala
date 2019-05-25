package application.commandhandler.action.add

import application.command.action.add.AddActionToStateCommand
import application.commandhandler.ActionTypeError
import domain.element.action.{Action, ActionType}
import domain.environment.Environment

class AddActionToStateHandler(environment: Environment) {

  /**
    *
    * @param addActionToStateCommand command
    * @return returns an exception or the name of the action, the name of the body action and the name of the prototype uri action
    */
  def execute(addActionToStateCommand: AddActionToStateCommand): Either[Exception, (String, String, String, String)] = {
    environment.getState(addActionToStateCommand.stateName) match {
      case Left(error) => Left(error)
      case Right(state) =>
        val actionType = addActionToStateCommand.actionType match {
          case application.command.action.modify.ActionType.ENTRY => ActionType.ENTRY
          case application.command.action.modify.ActionType.EXIT => ActionType.EXIT
          case application.command.action.modify.ActionType.GUARD => ActionType.GUARD
        }
        if (actionType == ActionType.GUARD) Left(new ActionTypeError("An Action for a State can't be of Guard type"))

        val action = new Action(actionType, environment)
        state.addAction(action) match {
          case Left(error) => Left(error)
          case Right(_) => Right(action.name, action.absoluteUri, action.body.name, action.prototypeUri.name)
        }
    }
  }
}
