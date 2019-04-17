package infrastructure.controller.prototypeuriparameter

import infrastructure.controller.DrawingPaneController
import infrastructure.elements.prototypeuriparameter.PrototypeUriParameter

class PrototypeUriParameterController(prototypeUriParameter: PrototypeUriParameter, drawingPaneController: DrawingPaneController) {
  private val propertiesBox = prototypeUriParameter.propertiesBox

  propertiesBox.setOnParameterPlaceholderChanged(placeholder => {
    //TODO: notify the model, ModifyPrototypeUriParameterPlaceholder
    prototypeUriParameter.placeholder = placeholder

    println("Parameter placeholder changed to -> " + placeholder)
  })

  propertiesBox.setOnParameterQueryChanged(query => {
    //TODO: notify the model, ModifyPrototypeUriParameterQuery
    prototypeUriParameter.query = query

    println("Parameter query changed to -> " + query)
  })

  propertiesBox.setOnRemoveParameterButtonClicked(() => {
    //TODO: notify the model, RemovePrototyeUriParameterFromPrototypeUri
    println("Removing a parameter")

    if (prototypeUriParameter.hasParent) {
      val prototypeUri = prototypeUriParameter.getParent

      var parameterList = prototypeUri.prototypeParameters
      parameterList = parameterList.filterNot(p => p == prototypeUriParameter)

      drawingPaneController.removePrototypeUriParameterFromPrototypeUri(prototypeUriParameter, prototypeUri)
    }
  })
}

object PrototypeUriParameterController {
  def addPrototypeUriParameterToPrototypeUri(): Unit = {

  }

  def modifyPrototypeUriParameterPlaceholder(): Unit = {

  }

  def modifyPrototypeUriParameterQuery(): Unit = {

  }

  def removePrototypeUriParameterFromPrototypeUri(): Unit = {

  }
}
