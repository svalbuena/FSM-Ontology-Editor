package infrastructure.elements.action

import infrastructure.drawingpane.shape.action.ActionPane
import infrastructure.elements.Element
import infrastructure.elements.action.ActionType.ActionType
import infrastructure.elements.action.MethodType.MethodType
import infrastructure.elements.action.body.{Body, BodyType}
import infrastructure.elements.action.uri.UriType
import infrastructure.elements.action.uri.UriType.UriType
import infrastructure.elements.action.uri.prototype.PrototypeUri
import infrastructure.menu.contextmenu.action.ActionContextMenu
import infrastructure.propertybox.action.ActionPropertiesBox

class Action(name: String,
             var actionType: ActionType = ActionType.ENTRY,
             var method: MethodType = MethodType.GET,
             var uriType: UriType = UriType.ABSOLUTE,
             var absoluteUri: String = "",
             var prototypeUri: PrototypeUri,
             var timeout: Int = 0,
             var body: Body
            ) extends Element(name) {

  val shape = new ActionPane()
  val propertiesBox = new ActionPropertiesBox(body.propertiesBox, prototypeUri.propertiesBox)
  val contextMenu = new ActionContextMenu

  var parent: Option[Element] = None

  def hasParent: Boolean = parent.isDefined

  def setParent(element: Element): Unit = parent = Some(element)

  def getParent: Element = parent.get
}
