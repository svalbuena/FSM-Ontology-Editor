package application.commandhandler.state.modify

import application.command.start.modify.ModifyStateTypeCommand
import domain.Environment
import domain.state.StateType

class ModifyStateTypeHandler {
  def execute(modifyStateTypeCommand: ModifyStateTypeCommand): Either[Exception, domain.state.StateType.StateType] = {
    Environment.getState(modifyStateTypeCommand.stateName) match {
      case Left(error) => Left(error)
      case Right(state) => state.stateType = modifyStateTypeCommand.stateType match {
        case application.command.start.modify.StateType.INITIAL => StateType.INITIAL
        case application.command.start.modify.StateType.SIMPLE => StateType.SIMPLE
        case application.command.start.modify.StateType.FINAL => StateType.FINAL
      }
    }
  }
}
