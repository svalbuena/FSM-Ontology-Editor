package infrastructure.controller.action

import infrastructure.controller.InfrastructureController
import infrastructure.elements.action.uri.prototype.PrototypeUri
import infrastructure.elements.action.uri.prototype.parameter.PrototypeParameter

class PrototypeUriListener(prototypeUri: PrototypeUri, infrastructureController: InfrastructureController) {
  private val propertiesBox = prototypeUri.propertiesBox

  propertiesBox.setOnStructureChanged(structure => {
    //TODO: notify the model
    prototypeUri.structure = structure
  })

  propertiesBox.setOnAddParameterButtonClicked(() => {
    val prototypeUriParameter = new PrototypeParameter("", "")

    var parameterList = prototypeUri.prototypeParameters
    parameterList = prototypeUriParameter :: parameterList

    infrastructureController.addPrototypeUriParameterToPrototypeUri(prototypeUriParameter, prototypeUri)
  })
}
