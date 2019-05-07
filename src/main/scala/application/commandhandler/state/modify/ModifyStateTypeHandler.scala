package application.commandhandler.state.modify

import application.command.state.modify
import application.command.state.modify.ModifyStateTypeCommand
import domain.Environment
import domain.state.StateType

class ModifyStateTypeHandler(environment: Environment) {

  /**
    *
    * @param modifyStateTypeCommand command
    * @return an exception or the state type
    */
  def execute(modifyStateTypeCommand: ModifyStateTypeCommand): Either[Exception, domain.state.StateType.StateType] = {
    environment.getState(modifyStateTypeCommand.stateName) match {
      case Left(error) => Left(error)
      case Right(state) => state.stateType = modifyStateTypeCommand.stateType match {
        case modify.StateType.INITIAL => StateType.INITIAL
        case modify.StateType.SIMPLE => StateType.SIMPLE
        case modify.StateType.FINAL => StateType.FINAL
        case modify.StateType.INITIAL_FINAL => StateType.INITIAL_FINAL
      }
    }
  }
}
