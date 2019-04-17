package infrastructure.element.prototypeuri

import infrastructure.element.Element
import infrastructure.element.prototypeuriparameter.PrototypeUriParameter
import infrastructure.propertybox.prototypeuri.PrototypeUriPropertiesBox

class PrototypeUri(name: String,
                   var structure: String = "",
                   var prototypeParameters: List[PrototypeUriParameter] = List()
                  ) extends Element(name) {
  val propertiesBox = new PrototypeUriPropertiesBox()

  for (prototypeParameter <- prototypeParameters) {
    prototypeParameter.setParent(this)
  }
}
