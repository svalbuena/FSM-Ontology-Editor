package infrastructure.controller.action

import infrastructure.controller.DrawingPaneController
import infrastructure.elements.action.uri.prototype.PrototypeUri
import infrastructure.elements.action.uri.prototype.parameter.PrototypeParameter

class PrototypeUriListener(prototypeUri: PrototypeUri, drawingPaneController: DrawingPaneController) {
  private val propertiesBox = prototypeUri.propertiesBox

  propertiesBox.setOnStructureChanged(structure => {
    //TODO: notify the model
    prototypeUri.structure = structure

    println("Structure name changed to -> " + structure)
  })

  propertiesBox.setOnAddParameterButtonClicked(() => {
    //TODO: notify the model
    val prototypeUriParameter = new PrototypeParameter("", "")

    var parameterList = prototypeUri.prototypeParameters
    parameterList = prototypeUriParameter :: parameterList

    println("Adding a parameter to a prototype uri")

    drawingPaneController.addPrototypeUriParameterToPrototypeUri(prototypeUriParameter, prototypeUri)
  })
}
