package infrastructure.controller.action

import infrastructure.controller.DrawingPaneController
import infrastructure.elements.action.uri.prototype.parameter.PrototypeParameter

class PrototypeParameterListener(prototypeUriParameter: PrototypeParameter, drawingPaneController: DrawingPaneController) {
  private val propertiesBox = prototypeUriParameter.propertiesBox

  propertiesBox.setOnParameterPlaceholderChanged(placeholder => {
    //TODO: notify the model
    prototypeUriParameter.placeholder = placeholder

    println("Parameter placeholder changed to -> " + placeholder)
  })

  propertiesBox.setOnParameterQueryChanged(query => {
    //TODO: notify the model
    prototypeUriParameter.query = query

    println("Parameter query changed to -> " + query)
  })

  propertiesBox.setOnRemoveParameterButtonClicked(() => {
    //TODO: notify the model
    println("Removing a parameter")

    if (prototypeUriParameter.hasParent) {
      val prototypeUri = prototypeUriParameter.getParent

      var parameterList = prototypeUri.prototypeParameters
      parameterList = parameterList.filterNot(p => p == prototypeUriParameter)

      drawingPaneController.removePrototypeUriParameterFromPrototypeUri(prototypeUriParameter, prototypeUri)
    }
  })
}
