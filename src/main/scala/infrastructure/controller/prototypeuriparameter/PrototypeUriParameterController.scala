package infrastructure.controller.prototypeuriparameter

import application.command.prototypeuriparameter.add.AddPrototypeUriParameterToPrototypeUriCommand
import application.command.prototypeuriparameter.modify.{ModifyPrototypeUriParameterPlaceholderCommand, ModifyPrototypeUriParameterQueryCommand}
import application.command.prototypeuriparameter.remove.RemovePrototypeUriParameterFromPrototypeUriCommand
import application.commandhandler.prototypeuriparameter.add.AddPrototypeUriParameterToPrototypeUriHandler
import application.commandhandler.prototypeuriparameter.modify.{ModifyPrototypeUriParameterPlaceholderHandler, ModifyPrototypeUriParameterQueryHandler}
import application.commandhandler.prototypeuriparameter.remove.RemovePrototypeUriParameterFromPrototypeUriHandler
import infrastructure.controller.DrawingPaneController
import infrastructure.element.prototypeuri.PrototypeUri
import infrastructure.element.prototypeuriparameter.PrototypeUriParameter

class PrototypeUriParameterController(prototypeUriParameter: PrototypeUriParameter, drawingPaneController: DrawingPaneController) {
  private val propertiesBox = prototypeUriParameter.propertiesBox

  propertiesBox.setOnParameterPlaceholderChanged(newPlaceholder => PrototypeUriParameterController.modifyPrototypeUriParameterPlaceholder(prototypeUriParameter, newPlaceholder))
  propertiesBox.setOnParameterQueryChanged(newQuery => PrototypeUriParameterController.modifyPrototypeUriParameterQuery(prototypeUriParameter, newQuery))
  propertiesBox.setOnRemoveParameterButtonClicked(() => removePrototypeUriParameter())

  private def removePrototypeUriParameter(): Unit = {
    if (prototypeUriParameter.hasParent) {
      val prototypeUri = prototypeUriParameter.getParent
      PrototypeUriParameterController.removePrototypeUriParameterFromPrototypeUri(prototypeUriParameter, prototypeUri, drawingPaneController)
    }
  }
}

object PrototypeUriParameterController {
  def addPrototypeUriParameterToPrototypeUri(prototypeUri: PrototypeUri, drawingPaneController: DrawingPaneController): Unit = {
    new AddPrototypeUriParameterToPrototypeUriHandler().execute(new AddPrototypeUriParameterToPrototypeUriCommand(prototypeUri.name)) match {
      case Left(error) => println(error.getMessage)
      case Right(parameterName) =>
        val prototypeUriParameter = new PrototypeUriParameter(parameterName)

        prototypeUri.prototypeParameters = prototypeUriParameter :: prototypeUri.prototypeParameters

        drawingPaneController.addPrototypeUriParameterToPrototypeUri(prototypeUriParameter, prototypeUri)

        println("Adding a parameter to a prototype uri")
    }
  }

  def modifyPrototypeUriParameterPlaceholder(prototypeUriParameter: PrototypeUriParameter, newPlaceholder: String): Unit = {
    new ModifyPrototypeUriParameterPlaceholderHandler().execute(new ModifyPrototypeUriParameterPlaceholderCommand(prototypeUriParameter.name, newPlaceholder)) match {
      case Left(error) => println(error.getMessage)
      case Right(_) =>
        prototypeUriParameter.placeholder = newPlaceholder

        println("Parameter placeholder changed to -> " + newPlaceholder)
    }
  }

  def modifyPrototypeUriParameterQuery(prototypeUriParameter: PrototypeUriParameter, newQuery: String): Unit = {
    new ModifyPrototypeUriParameterQueryHandler().execute(new ModifyPrototypeUriParameterQueryCommand(prototypeUriParameter.name, newQuery)) match {
      case Left(error) => println(error.getMessage)
      case Right(_) =>
        prototypeUriParameter.query = newQuery

        println("Parameter query changed to -> " + newQuery)
    }
  }

  def removePrototypeUriParameterFromPrototypeUri(prototypeUriParameter: PrototypeUriParameter, prototypeUri: PrototypeUri, drawingPaneController: DrawingPaneController): Unit = {
    new RemovePrototypeUriParameterFromPrototypeUriHandler().execute(new RemovePrototypeUriParameterFromPrototypeUriCommand(prototypeUriParameter.name, prototypeUri.name)) match {
      case Left(error) => println(error.getMessage)
      case Right(_) =>
        prototypeUri.prototypeParameters = prototypeUri.prototypeParameters.filterNot(p => p == prototypeUriParameter)

        drawingPaneController.removePrototypeUriParameterFromPrototypeUri(prototypeUriParameter, prototypeUri)

        println("Removing a parameter")
    }
  }
}
