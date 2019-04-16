package application.commandhandler.prototypeuriparameter.remove

import application.command.prototypeuriparameter.remove.RemovePrototypeUriParameterFromPrototypeUriCommand
import domain.Environment

class RemovePrototypeUriParameterFromPrototypeUriHandler {
  def execute(removePrototypeUriParameterFromActionCommand: RemovePrototypeUriParameterFromPrototypeUriCommand): Either[Exception, _] = {
    Environment.getPrototypeUriParameter(removePrototypeUriParameterFromActionCommand.parameterName) match {
      case Left(error) => Left(error)
      case Right(parameter) =>
        Environment.getPrototypeUri(removePrototypeUriParameterFromActionCommand.prototypeUriName) match {
          case Left(error) => Left(error)
          case Right(prototypeUri) => prototypeUri.removePrototypeUriParameter(parameter)
        }
    }
  }
}
