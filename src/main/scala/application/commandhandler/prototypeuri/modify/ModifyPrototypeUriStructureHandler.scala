package application.commandhandler.prototypeuri.modify

import application.command.prototypeuri.modify.ModifyPrototypeUriStructureCommand
import domain.Environment

class ModifyPrototypeUriStructureHandler(environment: Environment) {

  /**
    *
    * @param modifyPrototypeUriStructureCommand command
    * @return an exception or the structure
    */
  def execute(modifyPrototypeUriStructureCommand: ModifyPrototypeUriStructureCommand): Either[Exception, String] = {
    environment.getPrototypeUri(modifyPrototypeUriStructureCommand.prototypeUriName) match {
      case Left(error) => Left(error)
      case Right(prototypeUri) => prototypeUri.structure = modifyPrototypeUriStructureCommand.structure
    }
  }
}
