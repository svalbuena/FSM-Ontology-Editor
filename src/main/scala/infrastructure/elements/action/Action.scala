package infrastructure.elements.action

import infrastructure.elements.Element
import infrastructure.elements.action.ActionType.ActionType

class Action(id: String, var actionType: ActionType, var text: String) extends Element(id) {

}
