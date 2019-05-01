package application.commandhandler.body.modify

import application.command.body.modify.ModifyBodyTypeCommand
import domain.Environment
import domain.action.BodyType

class ModifyBodyTypeHandler {

  /**
    *
    * @param modifyBodyTypeCommand command
    * @return an exception or the body type
    */
  def execute(modifyBodyTypeCommand: ModifyBodyTypeCommand): Either[Exception, domain.action.BodyType.BodyType] = {
    Environment.getBody(modifyBodyTypeCommand.bodyName) match {
      case Left(error) => Left(error)
      case Right(body) => body.bodyType = modifyBodyTypeCommand.bodyType match {
        case application.command.body.modify.BodyType.RDF => BodyType.RDF
        case application.command.body.modify.BodyType.JSON => BodyType.JSON
        case application.command.body.modify.BodyType.SPARQL => BodyType.SPARQL
      }
    }
  }
}
