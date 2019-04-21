package infrastructure.controller.prototypeuri

import application.command.prototypeuri.modify.ModifyPrototypeUriStructureCommand
import application.commandhandler.prototypeuri.modify.ModifyPrototypeUriStructureHandler
import infrastructure.controller.prototypeuriparameter.PrototypeUriParameterController
import infrastructure.element.prototypeuri.PrototypeUri

class PrototypeUriController(prototypeUri: PrototypeUri) {
  private val propertiesBox = prototypeUri.propertiesBox

  propertiesBox.setOnStructureChanged(newStructure => PrototypeUriController.modifyPrototypeUriStructure(prototypeUri, newStructure))
  propertiesBox.setOnAddParameterButtonClicked(() => addPrototypeUriParameterToPrototypeUri())

  private def addPrototypeUriParameterToPrototypeUri(): Unit = PrototypeUriParameterController.addPrototypeUriParameterToPrototypeUri(prototypeUri)
}

object PrototypeUriController {
  def modifyPrototypeUriStructure(prototypeUri: PrototypeUri, newStructure: String): Unit = {
    new ModifyPrototypeUriStructureHandler().execute(new ModifyPrototypeUriStructureCommand(prototypeUri.name, newStructure)) match {
      case Left(error) => println(error.getMessage)
      case Right(_) =>
        prototypeUri.structure = newStructure

        println("Structure name changed to -> " + newStructure)
    }
  }

  def drawPrototypeUri(prototypeUri: PrototypeUri): Unit = {
    prototypeUri.propertiesBox.setStructure(prototypeUri.structure)

    for (parameter <- prototypeUri.prototypeParameters) {
      PrototypeUriParameterController.drawPrototypeUriParameter(parameter)
    }

    new PrototypeUriController(prototypeUri)
  }
}
