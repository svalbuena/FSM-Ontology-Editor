package application.commandhandler.state.modify

import application.command.state.modify.ModifyStateNameCommand
import domain.Environment

class ModifyStateNameHandler {
  def execute(modifyStateNameCommand: ModifyStateNameCommand): Either[Exception, String] = {
    Environment.getState(modifyStateNameCommand.stateName) match {
      case Left(error) => Left(error)
      case Right(state) => state.name = modifyStateNameCommand.newStateName
    }
  }
}
