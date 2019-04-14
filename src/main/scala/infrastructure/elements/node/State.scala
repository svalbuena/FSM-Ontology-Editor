package infrastructure.elements.node

import infrastructure.drawingpane.shape.state.StateShape
import infrastructure.elements.action.Action
import infrastructure.menu.contextmenu.state.StateContextMenu
import infrastructure.propertybox.state.StatePropertiesBox

class State(id: String,
            var name: String = "State",
            var actions: List[Action] = List()
           ) extends ConnectableElement(id) {
  val shape = new StateShape()
  val propertiesBox = new StatePropertiesBox()
  val contextMenu = new StateContextMenu()

  def setName(name: String): Unit = {
    this.name = name
    shape.setName(name)
    propertiesBox.setName(name)
  }
}
