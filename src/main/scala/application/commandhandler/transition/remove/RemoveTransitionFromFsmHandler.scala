package application.commandhandler.transition.remove

import application.command.transition.remove.RemoveTransitionFromFsmCommand
import domain.Environment

class RemoveTransitionFromFsmHandler(environment: Environment) {

  /**
    *
    * @param removeTransitionFromFsmCommand command
    * @return an exception or nothing if successful
    */
  def execute(removeTransitionFromFsmCommand: RemoveTransitionFromFsmCommand): Either[Exception, _] = {
    environment.getTransition(removeTransitionFromFsmCommand.transitionName) match {
      case Left(error) => Left(error)
      case Right(transition) =>
        environment.getSelectedFsm match {
          case Left(error) => Left(error)
          case Right(fsm) => fsm.removeTransition(transition)
        }
    }
  }
}
