package infrastructure.controller

import application.command.prototypeuriparameter.add.AddPrototypeUriParameterToPrototypeUriCommand
import application.command.prototypeuriparameter.modify.{ModifyPrototypeUriParameterNameCommand, ModifyPrototypeUriParameterPlaceholderCommand, ModifyPrototypeUriParameterQueryCommand}
import application.command.prototypeuriparameter.remove.RemovePrototypeUriParameterFromPrototypeUriCommand
import application.commandhandler.prototypeuriparameter.add.AddPrototypeUriParameterToPrototypeUriHandler
import application.commandhandler.prototypeuriparameter.modify.{ModifyPrototypeUriParameterNameHandler, ModifyPrototypeUriParameterPlaceholderHandler, ModifyPrototypeUriParameterQueryHandler}
import application.commandhandler.prototypeuriparameter.remove.RemovePrototypeUriParameterFromPrototypeUriHandler
import infrastructure.EnvironmentSingleton
import infrastructure.element.prototypeuri.PrototypeUri
import infrastructure.element.prototypeuriparameter.PrototypeUriParameter

/**
  * Controls the visual and behavior aspects of a prototype uri parameter
  *
  * @param prototypeUriParameter prototype uri parameter to control
  */
class PrototypeUriParameterController(prototypeUriParameter: PrototypeUriParameter) {
  private val propertiesBox = prototypeUriParameter.propertiesBox

  propertiesBox.setOnNameChanged(newName => PrototypeUriParameterController.modifyPrototypeUriParameterName(prototypeUriParameter, newName))
  propertiesBox.setOnParameterPlaceholderChanged(newPlaceholder => PrototypeUriParameterController.modifyPrototypeUriParameterPlaceholder(prototypeUriParameter, newPlaceholder))
  propertiesBox.setOnParameterQueryChanged(newQuery => PrototypeUriParameterController.modifyPrototypeUriParameterQuery(prototypeUriParameter, newQuery))
  propertiesBox.setOnRemoveParameterButtonClicked(() => removePrototypeUriParameter())

  private def removePrototypeUriParameter(): Unit = {
    val prototypeUri = prototypeUriParameter.parent
    PrototypeUriParameterController.removePrototypeUriParameterFromPrototypeUri(prototypeUriParameter, prototypeUri)
  }
}

/**
  * Operations that can be done with a Prototype Uri Parameter
  */
object PrototypeUriParameterController {
  private val environment = EnvironmentSingleton.get()

  /**
    * Creates a Prototype Uri Parameter to a Prototype Uri
    *
    * @param prototypeUri prototype uri where the parameter will be added
    */
  def addPrototypeUriParameterToPrototypeUri(prototypeUri: PrototypeUri): Unit = {
    new AddPrototypeUriParameterToPrototypeUriHandler(environment).execute(new AddPrototypeUriParameterToPrototypeUriCommand(prototypeUri.name)) match {
      case Left(error) => println(error.getMessage)
      case Right(parameterName) =>
        val prototypeUriParameter = new PrototypeUriParameter(parameterName, parent = prototypeUri)

        prototypeUri.addPrototypeUriParameter(prototypeUriParameter)

        drawPrototypeUriParameter(prototypeUriParameter)

        println("Adding a parameter to a prototype uri")
    }
  }

  /**
    * Draws a prototype uri parameter on the system
    *
    * @param prototypeUriParameter prototype uri parameter to be drawn
    */
  def drawPrototypeUriParameter(prototypeUriParameter: PrototypeUriParameter): Unit = {
    prototypeUriParameter.propertiesBox.setName(prototypeUriParameter.name)
    prototypeUriParameter.propertiesBox.setPlaceholder(prototypeUriParameter.placeholder)
    prototypeUriParameter.propertiesBox.setQuery(prototypeUriParameter.query)

    new PrototypeUriParameterController(prototypeUriParameter)
  }

  /**
    * Modifies the name of a prototype uri parameter
    *
    * @param prototypeUriParameter prototype uri parameter to be modified
    * @param newName               new name
    */
  def modifyPrototypeUriParameterName(prototypeUriParameter: PrototypeUriParameter, newName: String): Unit = {
    new ModifyPrototypeUriParameterNameHandler(environment).execute(new ModifyPrototypeUriParameterNameCommand(prototypeUriParameter.name, newName)) match {
      case Left(error) => println(error.getMessage)
      case Right(_) =>
        prototypeUriParameter.name = newName

        prototypeUriParameter.parent.propertiesBox.setParameterPropertiesBoxTitle(prototypeUriParameter.propertiesBox, prototypeUriParameter.name)

        println("Parameter name changed to -> " + newName)
    }
  }

  /**
    * Modifies the placeholder of a prototype uri parameter
    *
    * @param prototypeUriParameter prototype uri parameter to be modified
    * @param newPlaceholder        new placeholder
    */
  def modifyPrototypeUriParameterPlaceholder(prototypeUriParameter: PrototypeUriParameter, newPlaceholder: String): Unit = {
    new ModifyPrototypeUriParameterPlaceholderHandler(environment).execute(new ModifyPrototypeUriParameterPlaceholderCommand(prototypeUriParameter.name, newPlaceholder)) match {
      case Left(error) => println(error.getMessage)
      case Right(_) =>
        prototypeUriParameter.placeholder = newPlaceholder

        println("Parameter placeholder changed to -> " + newPlaceholder)
    }
  }

  /**
    * Modifies the query of a prototype uri parameter
    *
    * @param prototypeUriParameter prototype uri parameter to be modified
    * @param newQuery              new query
    */
  def modifyPrototypeUriParameterQuery(prototypeUriParameter: PrototypeUriParameter, newQuery: String): Unit = {
    new ModifyPrototypeUriParameterQueryHandler(environment).execute(new ModifyPrototypeUriParameterQueryCommand(prototypeUriParameter.name, newQuery)) match {
      case Left(error) => println(error.getMessage)
      case Right(_) =>
        prototypeUriParameter.query = newQuery

        println("Parameter query changed to -> " + newQuery)
    }
  }

  /**
    * Removes a prototype uri parameter from a prototype uri
    *
    * @param prototypeUriParameter prototype uri parameter to be removed
    * @param prototypeUri          prototype uri where the prototype uri parameter belongs
    */
  def removePrototypeUriParameterFromPrototypeUri(prototypeUriParameter: PrototypeUriParameter, prototypeUri: PrototypeUri): Unit = {
    new RemovePrototypeUriParameterFromPrototypeUriHandler(environment).execute(new RemovePrototypeUriParameterFromPrototypeUriCommand(prototypeUriParameter.name, prototypeUri.name)) match {
      case Left(error) => println(error.getMessage)
      case Right(_) =>
        prototypeUri.removePrototypeUriParameter(prototypeUriParameter)

        println("Removing a parameter")
    }
  }
}
