package application.commandhandler.prototypeuri.modify

import application.command.prototypeuri.modify.ModifyPrototypeUriNameCommand
import domain.environment.Environment

class ModifyPrototypeUriNameHandler(environment: Environment) {

  /**
    *
    * @param modifyPrototypeUriNameCommand command
    * @return an exception or prototype uri name
    */
  def execute(modifyPrototypeUriNameCommand: ModifyPrototypeUriNameCommand): Either[Exception, String] = {
    environment.getPrototypeUri(modifyPrototypeUriNameCommand.prototypeUriName) match {
      case Left(error) => Left(error)
      case Right(prototypeUri) =>
        prototypeUri.name = modifyPrototypeUriNameCommand.newPrototypeUriName
        Right(prototypeUri.name)
    }
  }
}
