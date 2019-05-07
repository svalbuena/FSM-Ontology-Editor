package infrastructure.element.prototypeuriparameter

import infrastructure.element.Element
import infrastructure.element.prototypeuri.PrototypeUri
import infrastructure.propertybox.prototypeuriparameter.PrototypeUriParameterPropertiesBox

/**
  * Parameter data
  *
  * @param name        name of the parameter
  * @param placeholder placeholder of the parameter
  * @param query       query of the parameter
  * @param parent      parent of the parameter
  */
class PrototypeUriParameter(name: String,
                            var placeholder: String = "",
                            var query: String = "",
                            val parent: PrototypeUri
                           ) extends Element(name) {

  val propertiesBox = new PrototypeUriParameterPropertiesBox()
}
