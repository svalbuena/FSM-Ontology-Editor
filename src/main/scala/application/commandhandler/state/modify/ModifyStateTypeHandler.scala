package application.commandhandler.state.modify

import application.command.state.modify
import application.command.state.modify.ModifyStateTypeCommand
import domain.Environment
import domain.state.StateType

class ModifyStateTypeHandler {
  def execute(modifyStateTypeCommand: ModifyStateTypeCommand): Either[Exception, domain.state.StateType.StateType] = {
    Environment.getState(modifyStateTypeCommand.stateName) match {
      case Left(error) => Left(error)
      case Right(state) => state.stateType = modifyStateTypeCommand.stateType match {
        case modify.StateType.INITIAL => StateType.INITIAL
        case modify.StateType.SIMPLE => StateType.SIMPLE
        case modify.StateType.FINAL => StateType.FINAL
      }
    }
  }
}
