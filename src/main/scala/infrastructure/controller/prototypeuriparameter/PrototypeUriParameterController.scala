package infrastructure.controller.prototypeuriparameter

import application.command.prototypeuriparameter.add.AddPrototypeUriParameterToPrototypeUriCommand
import application.command.prototypeuriparameter.modify.{ModifyPrototypeUriParameterPlaceholderCommand, ModifyPrototypeUriParameterQueryCommand}
import application.command.prototypeuriparameter.remove.RemovePrototypeUriParameterFromPrototypeUriCommand
import application.commandhandler.prototypeuriparameter.add.AddPrototypeUriParameterToPrototypeUriHandler
import application.commandhandler.prototypeuriparameter.modify.{ModifyPrototypeUriParameterPlaceholderHandler, ModifyPrototypeUriParameterQueryHandler}
import application.commandhandler.prototypeuriparameter.remove.RemovePrototypeUriParameterFromPrototypeUriHandler
import infrastructure.element.prototypeuri.PrototypeUri
import infrastructure.element.prototypeuriparameter.PrototypeUriParameter

class PrototypeUriParameterController(prototypeUriParameter: PrototypeUriParameter) {
  private val propertiesBox = prototypeUriParameter.propertiesBox

  propertiesBox.setOnParameterPlaceholderChanged(newPlaceholder => PrototypeUriParameterController.modifyPrototypeUriParameterPlaceholder(prototypeUriParameter, newPlaceholder))
  propertiesBox.setOnParameterQueryChanged(newQuery => PrototypeUriParameterController.modifyPrototypeUriParameterQuery(prototypeUriParameter, newQuery))
  propertiesBox.setOnRemoveParameterButtonClicked(() => removePrototypeUriParameter())

  private def removePrototypeUriParameter(): Unit = {
    if (prototypeUriParameter.hasParent) {
      val prototypeUri = prototypeUriParameter.getParent
      PrototypeUriParameterController.removePrototypeUriParameterFromPrototypeUri(prototypeUriParameter, prototypeUri)
    }
  }
}

object PrototypeUriParameterController {
  def addPrototypeUriParameterToPrototypeUri(prototypeUri: PrototypeUri): Unit = {
    new AddPrototypeUriParameterToPrototypeUriHandler().execute(new AddPrototypeUriParameterToPrototypeUriCommand(prototypeUri.name)) match {
      case Left(error) => println(error.getMessage)
      case Right(parameterName) =>
        val prototypeUriParameter = new PrototypeUriParameter(parameterName)
        prototypeUriParameter.setParent(prototypeUri)

        prototypeUri.addPrototypeUriParameter(prototypeUriParameter)

        drawPrototypeUriParameter(prototypeUriParameter)

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

  def removePrototypeUriParameterFromPrototypeUri(prototypeUriParameter: PrototypeUriParameter, prototypeUri: PrototypeUri): Unit = {
    new RemovePrototypeUriParameterFromPrototypeUriHandler().execute(new RemovePrototypeUriParameterFromPrototypeUriCommand(prototypeUriParameter.name, prototypeUri.name)) match {
      case Left(error) => println(error.getMessage)
      case Right(_) =>
        prototypeUri.removePrototypeUriParameter(prototypeUriParameter)

        println("Removing a parameter")
    }
  }

  def drawPrototypeUriParameter(prototypeUriParameter: PrototypeUriParameter): Unit = {
    prototypeUriParameter.propertiesBox.setQuery(prototypeUriParameter.query)
    prototypeUriParameter.propertiesBox.setPlaceholder(prototypeUriParameter.placeholder)

    new PrototypeUriParameterController(prototypeUriParameter)
  }
}