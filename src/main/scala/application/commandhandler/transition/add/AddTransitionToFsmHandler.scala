package application.commandhandler.transition.add

import application.command.transition.add.AddTransitionToFsmCommand
import domain.element.transition.Transition
import domain.environment.Environment

class AddTransitionToFsmHandler(environment: Environment) {

  /**
    *
    * @param addTransitionToFsmCommand command
    * @return an exception or the transition name
    */
  def execute(addTransitionToFsmCommand: AddTransitionToFsmCommand): Either[Exception, String] = {
    environment.getSelectedFsm match {
      case Left(error) => Left(error)
      case Right(fsm) =>
        environment.getState(addTransitionToFsmCommand.sourceStateName) match {
          case Left(error) => Left(error)
          case Right(source) =>
            environment.getState(addTransitionToFsmCommand.destinationStateName) match {
              case Left(error) => Left(error)
              case Right(destination) =>
                val transition = new Transition(source, destination, environment)
                fsm.addTransition(transition) match {
                  case Left(error) => Left(error)
                  case Right(_) => Right(transition.name)
                }
            }
        }
    }
  }
}
