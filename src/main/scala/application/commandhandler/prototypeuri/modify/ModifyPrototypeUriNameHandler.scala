package application.commandhandler.prototypeuri.modify

import application.command.prototypeuri.modify.ModifyPrototypeUriNameCommand
import domain.Environment

class ModifyPrototypeUriNameHandler {
  def execute(modifyPrototypeUriNameCommand: ModifyPrototypeUriNameCommand): Either[Exception, String] = {
    Environment.getPrototypeUri(modifyPrototypeUriNameCommand.prototypeUriName) match {
      case Left(error) => Left(error)
      case Right(prototypeUri) =>
        prototypeUri.name = modifyPrototypeUriNameCommand.newPrototypeUriName
        Right(prototypeUri.name)
    }
  }
}
