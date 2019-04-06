package infrastructure.controller.action

import infrastructure.controller.InfrastructureController
import infrastructure.elements.action.uri.prototype.parameter.PrototypeParameter

class PrototypeParameterListener(prototypeUriParameter: PrototypeParameter, infrastructureController: InfrastructureController) {
  private val propertiesBox = prototypeUriParameter.propertiesBox

  propertiesBox.setOnParameterPlaceholderChanged(placeholder => {
    //TODO: notify the model
    prototypeUriParameter.placeholder = placeholder
  })

  propertiesBox.setOnParameterQueryChanged(query => {
    //TODO: notify the model
    prototypeUriParameter.query = query
  })

  propertiesBox.setOnRemoveParameterButtonClicked(() => {
    if (prototypeUriParameter.hasParent) {
      val prototypeUri = prototypeUriParameter.getParent

      var parameterList = prototypeUri.prototypeParameters
      parameterList = parameterList.filterNot(p => p == prototypeUriParameter)

      infrastructureController.removePrototypeUriParameterFromPrototypeUri(prototypeUriParameter, prototypeUri)
    }
  })
}
