package infrastructure.elements.action.uri.prototype

import infrastructure.elements.action.uri.prototype.parameter.PrototypeParameter
import infrastructure.propertybox.action.section.uri.prototype.PrototypeUriPropertiesBox

class PrototypeUri(var structure: String = "",
                   var prototypeParameters: List[PrototypeParameter] = List()
                  ) {
  val propertiesBox = new PrototypeUriPropertiesBox()

  for (prototypeParameter <- prototypeParameters) {
    prototypeParameter.setParent(this)
  }
}
