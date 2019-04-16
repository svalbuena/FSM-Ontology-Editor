package application.commandhandler.transition.remove

import application.command.transition.remove.RemoveTransitionFromFsmCommand
import domain.Environment

class RemoveTransitionFromFsmHandler {
  def execute(removeTransitionFromFsmCommand: RemoveTransitionFromFsmCommand): Either[Exception, _] = {
    Environment.getTransition(removeTransitionFromFsmCommand.transitionName) match {
      case Left(error) => Left(error)
      case Right(transition) =>
        Environment.getSelectedFsm match {
          case Left(error) => Left(error)
          case Right(fsm) => fsm.removeTransition(transition)
        }
    }
  }
}
