package application.commandhandler.action.add

import application.command.action.add.AddActionToGuardCommand
import domain.Environment
import domain.action.{Action, ActionType}

class AddActionToGuardHandler {
  def execute(addActionToGuardCommand: AddActionToGuardCommand): Either[Exception, (String, String, String)] = {
    Environment.getGuard(addActionToGuardCommand.guardName) match {
      case Left(error) => Left(error)
      case Right(guard) =>
        val action = new Action(ActionType.GUARD)
        guard.addAction(action) match {
          case Left(error) => Left(error)
          case Right(_) => Right(action.name, action.body.name, action.prototypeUri.name)
        }
    }
  }
}
