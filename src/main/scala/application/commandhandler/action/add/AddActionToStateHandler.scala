package application.commandhandler.action.add

import application.command.action.add.AddActionToStateCommand
import domain.Environment
import domain.action.Action

class AddActionToStateHandler {

  def execute(addActionToStateCommand: AddActionToStateCommand): Either[Exception, String] = {
    Environment.getState(addActionToStateCommand.stateName) match {
      case Left(error) => Left(error)
      case Right(state) =>
        val action = new Action
        state.addAction(action) match {
          case Left(error) => Left(error)
          case Right(_) => Right(action.name)
        }
    }
  }
}
