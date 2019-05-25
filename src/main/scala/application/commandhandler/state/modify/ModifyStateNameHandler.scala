package application.commandhandler.state.modify

import application.command.state.modify.ModifyStateNameCommand
import domain.environment.Environment

class ModifyStateNameHandler(environment: Environment) {

  /**
    *
    * @param modifyStateNameCommand command
    * @return an exception or the state name
    */
  def execute(modifyStateNameCommand: ModifyStateNameCommand): Either[Exception, String] = {
    environment.getState(modifyStateNameCommand.stateName) match {
      case Left(error) => Left(error)
      case Right(state) => state.name = modifyStateNameCommand.newStateName
    }
  }
}
