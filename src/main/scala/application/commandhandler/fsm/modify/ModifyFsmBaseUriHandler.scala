package application.commandhandler.fsm.modify

import application.command.fsm.modify.ModifyFsmBaseUriCommand
import domain.Environment

class ModifyFsmBaseUriHandler {

  /**
    *
    * @param modifyFsmBaseUriCommand command
    * @return an exception or the base uri
    */
  def execute(modifyFsmBaseUriCommand: ModifyFsmBaseUriCommand): Either[Exception, String] = {
    Environment.getSelectedFsm match {
      case Left(error) => Left(error)
      case Right(fsm) => fsm.baseUri = modifyFsmBaseUriCommand.newFsmBaseUri
    }
  }
}
