package infrastructure.controller.prototypeuri

import infrastructure.controller.DrawingPaneController
import infrastructure.elements.prototypeuri.PrototypeUri
import infrastructure.elements.prototypeuriparameter.PrototypeUriParameter

class PrototypeUriController(prototypeUri: PrototypeUri, drawingPaneController: DrawingPaneController) {
  private val propertiesBox = prototypeUri.propertiesBox

  propertiesBox.setOnStructureChanged(structure => {
    //TODO: notify the model, ModifyPrototypeUriStructure
    prototypeUri.structure = structure

    println("Structure name changed to -> " + structure)
  })

  propertiesBox.setOnAddParameterButtonClicked(() => {
    //TODO: notify the model, AddPrototypeUriParameterToPrototypeUri
    val prototypeUriParameter = new PrototypeUriParameter("", "")

    var parameterList = prototypeUri.prototypeParameters
    parameterList = prototypeUriParameter :: parameterList

    println("Adding a parameter to a prototype uri")

    drawingPaneController.addPrototypeUriParameterToPrototypeUri(prototypeUriParameter, prototypeUri)
  })
}

object PrototypeUriController {
  def modifyPrototypeUriStructure(): Unit = {

  }
}
