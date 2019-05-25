package application.commandhandler.body.modify

import application.command.body.modify.ModifyBodyContentCommand
import domain.environment.Environment

class ModifyBodyContentHandler(environment: Environment) {

  /**
    *
    * @param modifyBodyContentCommand command
    * @return an exception or the content
    */
  def execute(modifyBodyContentCommand: ModifyBodyContentCommand): Either[Exception, String] = {
    environment.getBody(modifyBodyContentCommand.bodyName) match {
      case Left(error) => Left(error)
      case Right(body) => body.content = modifyBodyContentCommand.content
    }
  }
}
