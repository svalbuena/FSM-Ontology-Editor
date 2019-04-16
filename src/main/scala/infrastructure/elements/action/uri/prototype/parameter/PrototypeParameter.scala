package infrastructure.elements.action.uri.prototype.parameter

import infrastructure.elements.Element
import infrastructure.elements.action.uri.prototype.PrototypeUri
import infrastructure.propertybox.action.section.uri.prototype.parameter.PrototypeUriParameterPropertiesBox

class PrototypeParameter(name: String,
                         var query: String = "",
                         var placeholder: String = ""
                        ) extends Element(name) {
  val propertiesBox = new PrototypeUriParameterPropertiesBox()
  var parent: Option[PrototypeUri] = None

  def hasParent: Boolean = parent.isDefined

  def getParent: PrototypeUri = parent.get

  def setParent(prototypeUri: PrototypeUri): Unit = parent = Some(prototypeUri)
}
