package application.commandhandler.action.add

import application.command.action.add.AddActionToGuardCommand
import domain.element.action.{Action, ActionType}
import domain.environment.Environment

class AddActionToGuardHandler(environment: Environment) {

  /**
    *
    * @param addActionToGuardCommand command
    * @return returns an exception or the name of the action, the name of the body action and the name of the prototype uri action
    */
  def execute(addActionToGuardCommand: AddActionToGuardCommand): Either[Exception, (String, String, String, String)] = {
    environment.getGuard(addActionToGuardCommand.guardName) match {
      case Left(error) => Left(error)
      case Right(guard) =>
        val action = new Action(ActionType.GUARD, environment)
        guard.addAction(action) match {
          case Left(error) => Left(error)
          case Right(_) => Right(action.name, action.absoluteUri, action.body.name, action.prototypeUri.name)
        }
    }
  }
}
