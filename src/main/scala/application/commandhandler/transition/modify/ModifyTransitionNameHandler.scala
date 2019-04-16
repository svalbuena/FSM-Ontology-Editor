package application.commandhandler.transition.modify

import application.command.transition.modify.ModifyTransitionNameCommand
import domain.Environment

class ModifyTransitionNameHandler {
  def execute(modifyTransitionNameCommand: ModifyTransitionNameCommand): Either[Exception, String] = {
    Environment.getTransition(modifyTransitionNameCommand.transitionName) match {
      case Left(error) => Left(error)
      case Right(transition) => transition.name = modifyTransitionNameCommand.newTransitionName
    }
  }
}
