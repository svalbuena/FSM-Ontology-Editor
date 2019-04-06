package infrastructure.elements.action.uri.prototype

import infrastructure.elements.action.uri.prototype.parameter.PrototypeParameter
import infrastructure.propertybox.action.uri.prototype.PrototypeUriPropertiesBox

class PrototypeUri(var structure: String, var prototypeParameters: List[PrototypeParameter]) {
  val propertiesBox = new PrototypeUriPropertiesBox()
}
