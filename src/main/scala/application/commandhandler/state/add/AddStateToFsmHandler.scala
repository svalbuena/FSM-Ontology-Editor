package application.commandhandler.state.add

import application.command.state.add.AddStateToFsmCommand
import domain.element.state.State
import domain.environment.Environment

class AddStateToFsmHandler(environment: Environment) {

  /**
    *
    * @param addStateCommand command
    * @return an exception or the state name
    */
  def execute(addStateCommand: AddStateToFsmCommand): Either[Exception, String] = {
    environment.getSelectedFsm match {
      case Left(error) => Left(error)
      case Right(fsm) =>
        val state = new State(addStateCommand.x, addStateCommand.y, environment)
        fsm.addState(state) match {
          case Left(error) => Left(error)
          case Right(_) => Right(state.name)
        }
    }
  }
}
