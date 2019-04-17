package application.command.action.add

import infrastructure.elements.action.ActionType.ActionType

class AddActionToStateCommand(val actionType: ActionType, val stateName: String) {

}
