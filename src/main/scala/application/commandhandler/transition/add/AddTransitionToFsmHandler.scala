package application.commandhandler.transition.add

import application.command.transition.add.AddTransitionToFsmCommand
import domain.Environment
import domain.transition.Transition

class AddTransitionToFsmHandler {

  /**
    *
    * @param addTransitionToFsmCommand command
    * @return an exception or the transition name
    */
  def execute(addTransitionToFsmCommand: AddTransitionToFsmCommand): Either[Exception, String] = {
    Environment.getSelectedFsm match {
      case Left(error) => Left(error)
      case Right(fsm) =>
        Environment.getState(addTransitionToFsmCommand.sourceStateName) match {
          case Left(error) => Left(error)
          case Right(source) =>
            Environment.getState(addTransitionToFsmCommand.destinationStateName) match {
              case Left(error) => Left(error)
              case Right(destination) =>
                val transition = new Transition(source, destination)
                fsm.addTransition(transition) match {
                  case Left(error) => Left(error)
                  case Right(_) => Right(transition.name)
                }
            }
        }
    }
  }
}
