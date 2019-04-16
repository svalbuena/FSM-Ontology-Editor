package application.commandhandler.prototypeuri.modify

import application.command.prototypeuri.modify.ModifyPrototypeUriStructureCommand
import domain.Environment

class ModifyPrototypeUriStructureHandler {
  def execute(modifyPrototypeUriStructureCommand: ModifyPrototypeUriStructureCommand): Either[Exception, String] = {
    Environment.getPrototypeUri(modifyPrototypeUriStructureCommand.prototypeUriName) match {
      case Left(error) => Left(error)
      case Right(prototypeUri) => prototypeUri.structure = modifyPrototypeUriStructureCommand.structure
    }
  }
}
