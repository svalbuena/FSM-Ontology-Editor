package infrastructure.controller

import application.command.prototypeuri.modify.{ModifyPrototypeUriNameCommand, ModifyPrototypeUriStructureCommand}
import application.commandhandler.prototypeuri.modify.{ModifyPrototypeUriNameHandler, ModifyPrototypeUriStructureHandler}
import infrastructure.element.prototypeuri.PrototypeUri

class PrototypeUriController(prototypeUri: PrototypeUri) {
  private val propertiesBox = prototypeUri.propertiesBox

  propertiesBox.setOnNameChanged(newName => PrototypeUriController.modifyPrototypeUriName(prototypeUri, newName))
  propertiesBox.setOnStructureChanged(newStructure => PrototypeUriController.modifyPrototypeUriStructure(prototypeUri, newStructure))
  propertiesBox.setOnAddParameterButtonClicked(() => addPrototypeUriParameterToPrototypeUri())

  private def addPrototypeUriParameterToPrototypeUri(): Unit = PrototypeUriParameterController.addPrototypeUriParameterToPrototypeUri(prototypeUri)
}

object PrototypeUriController {
  def modifyPrototypeUriName(prototypeUri: PrototypeUri, newName: String): Unit = {
    new ModifyPrototypeUriNameHandler().execute(new ModifyPrototypeUriNameCommand(prototypeUri.name, newName)) match {
      case Left(error) => println(error.getMessage)
      case Right(_) =>
        prototypeUri.name = newName

        println("PrototypeUri name changed to -> " + newName)
    }
  }

  def modifyPrototypeUriStructure(prototypeUri: PrototypeUri, newStructure: String): Unit = {
    new ModifyPrototypeUriStructureHandler().execute(new ModifyPrototypeUriStructureCommand(prototypeUri.name, newStructure)) match {
      case Left(error) => println(error.getMessage)
      case Right(_) =>
        prototypeUri.structure = newStructure

        println("Structure name changed to -> " + newStructure)
    }
  }

  def drawPrototypeUri(prototypeUri: PrototypeUri): Unit = {
    prototypeUri.propertiesBox.setName(prototypeUri.name)
    prototypeUri.propertiesBox.setStructure(prototypeUri.structure)

    for (parameter <- prototypeUri.prototypeParameters) {
      PrototypeUriParameterController.drawPrototypeUriParameter(parameter)
    }

    new PrototypeUriController(prototypeUri)
  }
}
