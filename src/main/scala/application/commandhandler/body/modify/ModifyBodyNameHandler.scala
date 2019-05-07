package application.commandhandler.body.modify

import application.command.body.modify.ModifyBodyNameCommand
import domain.Environment

class ModifyBodyNameHandler(environment: Environment) {

  /**
    *
    * @param modifyBodyNameCommand command
    * @return an exception or the body name
    */
  def execute(modifyBodyNameCommand: ModifyBodyNameCommand): Either[Exception, String] = {
    environment.getBody(modifyBodyNameCommand.bodyName) match {
      case Left(error) => Left(error)
      case Right(body) => body.name = modifyBodyNameCommand.newBodyName
    }
  }
}
