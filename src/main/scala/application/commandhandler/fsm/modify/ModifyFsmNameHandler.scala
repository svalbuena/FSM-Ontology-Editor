package application.commandhandler.fsm.modify

import application.command.fsm.modify.ModifyFsmNameCommand
import domain.Environment

class ModifyFsmNameHandler {

  /**
    *
    * @param modifyFsmNameCommand command
    * @return an exception or the fsm name
    */
  def execute(modifyFsmNameCommand: ModifyFsmNameCommand): Either[Exception, String] = {
    Environment.getSelectedFsm match {
      case Left(error) => Left(error)
      case Right(fsm) => fsm.name = modifyFsmNameCommand.newFsmName
    }
  }
}
