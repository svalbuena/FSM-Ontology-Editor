package infrastructure.element.state

import infrastructure.drawingpane.shape.state.StateShape
import infrastructure.element.ConnectableElement
import infrastructure.element.action.Action
import infrastructure.element.fsm.FiniteStateMachine
import infrastructure.element.state.StateType.StateType
import infrastructure.menu.contextmenu.state.StateContextMenu
import infrastructure.propertybox.state.StatePropertiesBox

class State(name: String,
            var x: Double,
            var y: Double,
            var stateType: StateType = StateType.SIMPLE,
            val parent: FiniteStateMachine
           ) extends ConnectableElement(name) {

  var actions: List[Action] = List()

  val shape = new StateShape()
  val propertiesBox = new StatePropertiesBox()
  val contextMenu = new StateContextMenu()

  def addAction(action: Action): Unit = {
    actions = action :: actions

    propertiesBox.addAction(action.propertiesBox, action.actionType, action.name)
    shape.addAction(action.shape, action.actionType)
  }

  def removeAction(action: Action): Unit = {
    actions = actions.filterNot(a => a == action)

    propertiesBox.removeAction(action.propertiesBox, action.actionType)
    shape.removeAction(action.shape, action.actionType)
  }
}
