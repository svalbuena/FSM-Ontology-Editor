package application.commandhandler.prototypeuriparameter.remove

import application.command.prototypeuriparameter.remove.RemovePrototypeUriParameterFromPrototypeUriCommand
import domain.Environment

class RemovePrototypeUriParameterFromPrototypeUriHandler(environment: Environment) {

  /**
    *
    * @param removePrototypeUriParameterFromActionCommand command
    * @return an exception or nothing if successful
    */
  def execute(removePrototypeUriParameterFromActionCommand: RemovePrototypeUriParameterFromPrototypeUriCommand): Either[Exception, _] = {
    environment.getPrototypeUriParameter(removePrototypeUriParameterFromActionCommand.parameterName) match {
      case Left(error) => Left(error)
      case Right(parameter) =>
        environment.getPrototypeUri(removePrototypeUriParameterFromActionCommand.prototypeUriName) match {
          case Left(error) => Left(error)
          case Right(prototypeUri) => prototypeUri.removePrototypeUriParameter(parameter)
        }
    }
  }
}
