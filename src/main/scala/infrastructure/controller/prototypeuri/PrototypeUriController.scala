package infrastructure.controller.prototypeuri

import application.command.prototypeuri.modify.ModifyPrototypeUriStructureCommand
import application.commandhandler.prototypeuri.modify.ModifyPrototypeUriStructureHandler
import infrastructure.controller.DrawingPaneController
import infrastructure.controller.prototypeuriparameter.PrototypeUriParameterController
import infrastructure.element.prototypeuri.PrototypeUri

class PrototypeUriController(prototypeUri: PrototypeUri, drawingPaneController: DrawingPaneController) {
  private val propertiesBox = prototypeUri.propertiesBox

  propertiesBox.setOnStructureChanged(newStructure => PrototypeUriController.modifyPrototypeUriStructure(prototypeUri, newStructure))
  propertiesBox.setOnAddParameterButtonClicked(() => addPrototypeUriParameterToPrototypeUri())

  private def addPrototypeUriParameterToPrototypeUri(): Unit = PrototypeUriParameterController.addPrototypeUriParameterToPrototypeUri(prototypeUri, drawingPaneController)
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
}
