package application.commandhandler.guard.add

import application.command.guard.add.AddGuardToTransitionCommand
import domain.Environment
import domain.guard.Guard

class AddGuardToTransitionHandler {

  /**
    *
    * @param addGuardToTransitionCommand command
    * @return an exception or the guard name
    */
  def execute(addGuardToTransitionCommand: AddGuardToTransitionCommand): Either[Exception, String] = {
    Environment.getTransition(addGuardToTransitionCommand.transitionName) match {
      case Left(error) => Left(error)
      case Right(transition) =>
        val guard = new Guard
        transition.addGuard(guard) match {
          case Left(error) => Left(error)
          case Right(_) => Right(guard.name)
        }
    }
  }
}
