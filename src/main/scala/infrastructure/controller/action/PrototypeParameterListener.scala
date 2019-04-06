package infrastructure.controller.action

import infrastructure.elements.action.uri.prototype.parameter.PrototypeParameter

class PrototypeParameterListener(prototypeParameter: PrototypeParameter) {
  private val propertiesBox = prototypeParameter.propertiesBox

  propertiesBox.setOnParameterPlaceholderChanged(placeholder => {
    //TODO: notify the model
    prototypeParameter.placeholder = placeholder
  })

  propertiesBox.setOnParameterQueryChanged(query => {
    //TODO: notify the model
    prototypeParameter.query = query
  })
}
