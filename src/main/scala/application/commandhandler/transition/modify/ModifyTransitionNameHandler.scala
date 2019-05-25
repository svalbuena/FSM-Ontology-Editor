package application.commandhandler.transition.modify

import application.command.transition.modify.ModifyTransitionNameCommand
import domain.environment.Environment

class ModifyTransitionNameHandler(environment: Environment) {

  /**
    *
    * @param modifyTransitionNameCommand command
    * @return an exception or the transition name
    */
  def execute(modifyTransitionNameCommand: ModifyTransitionNameCommand): Either[Exception, String] = {
    environment.getTransition(modifyTransitionNameCommand.transitionName) match {
      case Left(error) => Left(error)
      case Right(transition) => transition.name = modifyTransitionNameCommand.newTransitionName
    }
  }
}
