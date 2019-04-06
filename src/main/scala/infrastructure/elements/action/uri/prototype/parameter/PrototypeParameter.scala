package infrastructure.elements.action.uri.prototype.parameter

import infrastructure.propertybox.action.uri.prototype.parameter.PrototypeParameterPropertiesBox

class PrototypeParameter(var query: String, var placeholder: String) {
  val propertiesBox = new PrototypeParameterPropertiesBox()
}
