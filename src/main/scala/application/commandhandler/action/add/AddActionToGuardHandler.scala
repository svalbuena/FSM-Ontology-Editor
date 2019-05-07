package application.commandhandler.action.add

import application.command.action.add.AddActionToGuardCommand
import domain.Environment
import domain.action.{Action, ActionType}

class AddActionToGuardHandler(environment: Environment) {

  /**
    *
    * @param addActionToGuardCommand command
    * @return returns an exception or the name of the action, the name of the body action and the name of the prototype uri action
    */
  def execute(addActionToGuardCommand: AddActionToGuardCommand): Either[Exception, (String, String, String)] = {
    environment.getGuard(addActionToGuardCommand.guardName) match {
      case Left(error) => Left(error)
      case Right(guard) =>
        val action = new Action(ActionType.GUARD, environment)
        guard.addAction(action) match {
          case Left(error) => Left(error)
          case Right(_) => Right(action.name, action.body.name, action.prototypeUri.name)
        }
    }
  }
}
