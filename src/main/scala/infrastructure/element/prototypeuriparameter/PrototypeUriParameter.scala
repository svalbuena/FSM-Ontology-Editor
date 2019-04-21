package infrastructure.element.prototypeuriparameter

import infrastructure.element.Element
import infrastructure.element.prototypeuri.PrototypeUri
import infrastructure.propertybox.prototypeuriparameter.PrototypeUriParameterPropertiesBox

class PrototypeUriParameter(name: String,
                            var query: String = "",
                            var placeholder: String = "",
                            val parent: PrototypeUri
                           ) extends Element(name) {

  val propertiesBox = new PrototypeUriParameterPropertiesBox()
}
