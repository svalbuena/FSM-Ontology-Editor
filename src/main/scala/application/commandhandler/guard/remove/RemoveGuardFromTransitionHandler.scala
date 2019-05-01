package application.commandhandler.guard.remove

import application.command.guard.remove.RemoveGuardFromTransitionCommand
import domain.Environment

class RemoveGuardFromTransitionHandler {

  /**
    *
    * @param removeGuardFromTransitionCommand command
    * @return an exception or nothing if successful
    */
  def execute(removeGuardFromTransitionCommand: RemoveGuardFromTransitionCommand): Either[Exception, _] = {
    Environment.getGuard(removeGuardFromTransitionCommand.guardName) match {
      case Left(error) => Left(error)
      case Right(guard) =>
        Environment.getTransition(removeGuardFromTransitionCommand.transitionName) match {
          case Left(error) => Left(error)
          case Right(transition) => transition.removeGuard(guard)
        }
    }
  }
}
