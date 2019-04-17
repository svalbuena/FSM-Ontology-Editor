package infrastructure.elements.state

import infrastructure.drawingpane.shape.state.StateShape
import infrastructure.elements.ConnectableElement
import infrastructure.elements.action.Action
import infrastructure.menu.contextmenu.state.StateContextMenu
import infrastructure.propertybox.state.StatePropertiesBox

class State(name: String,
            var actions: List[Action] = List()
           ) extends ConnectableElement(name) {
  val shape = new StateShape()
  val propertiesBox = new StatePropertiesBox()
  val contextMenu = new StateContextMenu()
}
