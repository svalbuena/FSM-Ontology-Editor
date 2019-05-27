package infrastructure.controller

import application.command.prototypeuri.modify.{ModifyPrototypeUriNameCommand, ModifyPrototypeUriStructureCommand}
import application.commandhandler.prototypeuri.modify.{ModifyPrototypeUriNameHandler, ModifyPrototypeUriStructureHandler}
import infrastructure.element.prototypeuri.PrototypeUri
import infrastructure.main.EnvironmentSingleton

/**
  * Controls the visual and behavior aspects of a prototype uri
  *
  * @param prototypeUri prototype uri to control
  */
class PrototypeUriController(prototypeUri: PrototypeUri) {
  private val propertiesBox = prototypeUri.propertiesBox

  propertiesBox.setOnNameChanged(newName => PrototypeUriController.modifyPrototypeUriName(prototypeUri, newName))
  propertiesBox.setOnStructureChanged(newStructure => PrototypeUriController.modifyPrototypeUriStructure(prototypeUri, newStructure))
  propertiesBox.setOnAddParameterButtonClicked(() => addPrototypeUriParameterToPrototypeUri())

  private def addPrototypeUriParameterToPrototypeUri(): Unit = PrototypeUriParameterController.addPrototypeUriParameterToPrototypeUri(prototypeUri)
}

/**
  * Operations that can be done with a Prototype Uri
  */
object PrototypeUriController {
  private val environment = EnvironmentSingleton.get()

  /**
    * Modifies a prototype uri name
    *
    * @param prototypeUri prototype uri to be modified
    * @param newName      new name
    */
  def modifyPrototypeUriName(prototypeUri: PrototypeUri, newName: String): Unit = {
    new ModifyPrototypeUriNameHandler(environment).execute(new ModifyPrototypeUriNameCommand(prototypeUri.name, newName)) match {
      case Left(error) => println(error.getMessage)
      case Right(_) =>
        prototypeUri.name = newName

        println("PrototypeUri name changed to -> " + newName)
    }
  }

  /**
    * Modifies a prototype uri structure
    *
    * @param prototypeUri prototype uri to be modified
    * @param newStructure new structure
    */
  def modifyPrototypeUriStructure(prototypeUri: PrototypeUri, newStructure: String): Unit = {
    new ModifyPrototypeUriStructureHandler(environment).execute(new ModifyPrototypeUriStructureCommand(prototypeUri.name, newStructure)) match {
      case Left(error) => println(error.getMessage)
      case Right(_) =>
        prototypeUri.structure = newStructure

        println("Structure name changed to -> " + newStructure)
    }
  }

  /**
    * Draws a prototype uri to the canvas
    *
    * @param prototypeUri prototype uri to be drawn
    */
  def drawPrototypeUri(prototypeUri: PrototypeUri): Unit = {
    prototypeUri.propertiesBox.setName(prototypeUri.name)
    prototypeUri.propertiesBox.setStructure(prototypeUri.structure)

    for (parameter <- prototypeUri.prototypeParameters) {
      PrototypeUriParameterController.drawPrototypeUriParameter(parameter)
    }

    new PrototypeUriController(prototypeUri)
  }
}
