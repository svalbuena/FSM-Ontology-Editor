package application.commandhandler.body.modify

import application.command.body.modify.ModifyBodyNameCommand
import domain.Environment

class ModifyBodyNameHandler {
  def execute(modifyBodyNameCommand: ModifyBodyNameCommand): Either[Exception, String] = {
    Environment.getBody(modifyBodyNameCommand.bodyName) match {
      case Left(error) => Left(error)
      case Right(body) => body.name = modifyBodyNameCommand.newBodyName
    }
  }
}
