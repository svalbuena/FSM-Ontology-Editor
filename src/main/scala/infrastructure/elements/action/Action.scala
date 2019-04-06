package infrastructure.elements.action

import infrastructure.drawingpane.shape.state.action.ActionPane
import infrastructure.elements.Element
import infrastructure.elements.action.ActionType.ActionType
import infrastructure.elements.action.body.{Body, BodyType}
import infrastructure.elements.action.uri.UriType
import infrastructure.elements.action.uri.UriType.UriType
import infrastructure.elements.action.uri.prototype.PrototypeUri
import infrastructure.menu.contextmenu.action.ActionContextMenu
import infrastructure.propertybox.action.ActionPropertiesBox

class Action(id: String, var actionType: ActionType, var name: String, var uriType: UriType, var absoluteUri: String, var prototypeUri: PrototypeUri, var body: Body) extends Element(id) {
  val stateActionPane = new ActionPane()
  val propertiesBox = new ActionPropertiesBox(body.propertiesBox, prototypeUri.propertiesBox)
  val contextMenu = new ActionContextMenu

  var parent: Option[Element] = None


  def this(id: String, actionType: ActionType, name: String, absoluteUri: String, body: Body) = this(id, actionType, name, UriType.ABSOLUTE, absoluteUri, new PrototypeUri("", List()), body)
  def this(id: String, actionType: ActionType, name: String, prototypeUri: PrototypeUri, body: Body) = this(id, actionType, name, UriType.PROTOTYPE, "", prototypeUri, body)
  def this(id: String, actionType: ActionType, name: String) = this(id, actionType, name, UriType.ABSOLUTE, "", new PrototypeUri("", List()), new Body(BodyType.RDF, ""))

  def hasParent: Boolean = {
    parent.isDefined
  }

  def setParent(element: Element): Unit = {
    parent = Some(element)
  }

  def getParent: Element = {
    parent.get
  }
}
