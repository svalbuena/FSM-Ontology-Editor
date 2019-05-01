package application.commandhandler.body.modify

import application.command.body.modify.ModifyBodyContentCommand
import domain.Environment

class ModifyBodyContentHandler {

  /**
    *
    * @param modifyBodyContentCommand command
    * @return an exception or the content
    */
  def execute(modifyBodyContentCommand: ModifyBodyContentCommand): Either[Exception, String] = {
    Environment.getBody(modifyBodyContentCommand.bodyName) match {
      case Left(error) => Left(error)
      case Right(body) => body.content = modifyBodyContentCommand.content
    }
  }
}
