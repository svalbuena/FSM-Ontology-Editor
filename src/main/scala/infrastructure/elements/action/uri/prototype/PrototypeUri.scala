package infrastructure.elements.action.uri.prototype

import infrastructure.elements.Element
import infrastructure.elements.action.uri.prototype.parameter.PrototypeParameter
import infrastructure.propertybox.action.section.uri.prototype.PrototypeUriPropertiesBox

class PrototypeUri(name: String,
                   var structure: String = "",
                   var prototypeParameters: List[PrototypeParameter] = List()
                  ) extends Element(name) {
  val propertiesBox = new PrototypeUriPropertiesBox()

  for (prototypeParameter <- prototypeParameters) {
    prototypeParameter.setParent(this)
  }
}
