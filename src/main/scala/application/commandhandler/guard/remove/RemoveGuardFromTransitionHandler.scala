package application.commandhandler.guard.remove

import application.command.guard.remove.RemoveGuardFromTransitionCommand
import domain.environment.Environment

class RemoveGuardFromTransitionHandler(environment: Environment) {

  /**
    *
    * @param removeGuardFromTransitionCommand command
    * @return an exception or nothing if successful
    */
  def execute(removeGuardFromTransitionCommand: RemoveGuardFromTransitionCommand): Either[Exception, _] = {
    environment.getGuard(removeGuardFromTransitionCommand.guardName) match {
      case Left(error) => Left(error)
      case Right(guard) =>
        environment.getTransition(removeGuardFromTransitionCommand.transitionName) match {
          case Left(error) => Left(error)
          case Right(transition) => transition.removeGuard(guard)
        }
    }
  }
}
