package infrastructure.element.action

import infrastructure.drawingpane.shape.action.ActionPane
import infrastructure.element.Element
import infrastructure.element.action.ActionType.ActionType
import infrastructure.element.action.MethodType.MethodType
import infrastructure.element.action.UriType.UriType
import infrastructure.element.body.Body
import infrastructure.element.prototypeuri.PrototypeUri
import infrastructure.menu.contextmenu.action.ActionContextMenu
import infrastructure.propertybox.action.ActionPropertiesBox

class Action(name: String, var actionType: ActionType = ActionType.ENTRY, var method: MethodType = MethodType.GET, var body: Body, var uriType: UriType = UriType.ABSOLUTE, var absoluteUri: String = "", var prototypeUri: PrototypeUri, var timeout: String = "0", val parent: Element) extends Element(name) {

  val shape = new ActionPane()
  val propertiesBox = new ActionPropertiesBox(body.propertiesBox, prototypeUri.propertiesBox)
  val contextMenu = new ActionContextMenu
}
