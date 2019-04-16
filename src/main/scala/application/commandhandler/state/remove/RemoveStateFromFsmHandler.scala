package application.commandhandler.state.remove

import application.command.state.remove.RemoveStateFromFsmCommand
import domain.Environment

class RemoveStateFromFsmHandler {
  def execute(removeStateFromFsmCommand: RemoveStateFromFsmCommand): Either[Exception, _] = {
    Environment.getState(removeStateFromFsmCommand.stateName) match {
      case Left(error) => Left(error)
      case Right(state) =>
        Environment.getSelectedFsm match {
          case Left(error) => Left(error)
          case Right(fsm) => fsm.removeState(state)
        }
    }
  }
}
