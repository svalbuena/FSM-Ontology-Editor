package application.commandhandler.state.add

import application.command.state.add.AddStateToFsmCommand
import domain.Environment
import domain.state.State

class AddStateToFsmHandler {
  def execute(addStateCommand: AddStateToFsmCommand): Either[Exception, String] = {
    Environment.getSelectedFsm match {
      case Left(error) => Left(error)
      case Right(fsm) =>
        val state = new State(addStateCommand.x, addStateCommand.y)
        fsm.addState(state) match {
          case Left(error) => Left(error)
          case Right(_) => Right(state.name)
        }
    }
  }
}
