package infrastructure.elements.action.uri.prototype.parameter

import infrastructure.elements.action.Action
import infrastructure.elements.action.uri.prototype.PrototypeUri
import infrastructure.propertybox.action.uri.prototype.parameter.PrototypeUriParameterPropertiesBox

class PrototypeParameter(var query: String, var placeholder: String) {
  val propertiesBox = new PrototypeUriParameterPropertiesBox()
  var parent: Option[PrototypeUri] = None

  def hasParent: Boolean = parent.isDefined
  def getParent: PrototypeUri = parent.get
  def setParent(prototypeUri: PrototypeUri): Unit = parent = Some(prototypeUri)
}
