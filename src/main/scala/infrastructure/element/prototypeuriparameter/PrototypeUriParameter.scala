package infrastructure.element.prototypeuriparameter

import infrastructure.element.Element
import infrastructure.element.prototypeuri.PrototypeUri
import infrastructure.propertybox.prototypeuriparameter.PrototypeUriParameterPropertiesBox

class PrototypeUriParameter(name: String,
                            var query: String = "",
                            var placeholder: String = ""
                           ) extends Element(name) {
  val propertiesBox = new PrototypeUriParameterPropertiesBox()
  var parent: Option[PrototypeUri] = None

  def hasParent: Boolean = parent.isDefined

  def getParent: PrototypeUri = parent.get

  def setParent(prototypeUri: PrototypeUri): Unit = parent = Some(prototypeUri)
}
