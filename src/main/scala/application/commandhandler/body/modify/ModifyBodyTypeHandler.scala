package application.commandhandler.body.modify

import application.command.body.modify.ModifyBodyTypeCommand
import domain.element.action.BodyType
import domain.environment.Environment

class ModifyBodyTypeHandler(environment: Environment) {

  /**
    *
    * @param modifyBodyTypeCommand command
    * @return an exception or the body type
    */
  def execute(modifyBodyTypeCommand: ModifyBodyTypeCommand): Either[Exception, domain.element.action.BodyType.BodyType] = {
    environment.getBody(modifyBodyTypeCommand.bodyName) match {
      case Left(error) => Left(error)
      case Right(body) => body.bodyType = modifyBodyTypeCommand.bodyType match {
        case application.command.body.modify.BodyType.RDF => BodyType.RDF
        case application.command.body.modify.BodyType.JSON => BodyType.JSON
        case application.command.body.modify.BodyType.SPARQL => BodyType.SPARQL
      }
    }
  }
}
