package application.commandhandler.state.modify

import application.command.state.modify.ModifyStateCoordinatesCommand
import domain.Environment

class ModifyStateCoordinatesHandler {
  def execute(modifyStateCoordinatesCommand: ModifyStateCoordinatesCommand): Either[Exception, _] = {
    Environment.getState(modifyStateCoordinatesCommand.stateName) match {
      case Left(error) => Left(error)
      case Right(state) =>
        state.x = modifyStateCoordinatesCommand.newX
        state.y = modifyStateCoordinatesCommand.newY
        Right(())
    }
  }
}
