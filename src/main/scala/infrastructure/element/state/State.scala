package infrastructure.element.state

import infrastructure.drawingpane.shape.state.StateShape
import infrastructure.element.ConnectableElement
import infrastructure.element.action.Action
import infrastructure.element.state.StateType.StateType
import infrastructure.menu.contextmenu.state.StateContextMenu
import infrastructure.propertybox.state.StatePropertiesBox

class State(name: String,
            var stateType: StateType = StateType.SIMPLE,
            var actions: List[Action] = List()
           ) extends ConnectableElement(name) {

  val shape = new StateShape()
  val propertiesBox = new StatePropertiesBox()
  val contextMenu = new StateContextMenu()
}
