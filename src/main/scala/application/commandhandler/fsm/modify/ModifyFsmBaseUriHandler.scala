package application.commandhandler.fsm.modify

import application.command.fsm.modify.ModifyFsmBaseUriCommand
import domain.environment.Environment

class ModifyFsmBaseUriHandler(environment: Environment) {

  /**
    *
    * @param modifyFsmBaseUriCommand command
    * @return an exception or the base uri
    */
  def execute(modifyFsmBaseUriCommand: ModifyFsmBaseUriCommand): Either[Exception, String] = {
    environment.getSelectedFsm match {
      case Left(error) => Left(error)
      case Right(fsm) => fsm.baseUri = modifyFsmBaseUriCommand.newFsmBaseUri
    }
  }
}
