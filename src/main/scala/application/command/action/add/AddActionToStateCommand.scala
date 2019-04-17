package application.command.action.add

import infrastructure.element.action.ActionType.ActionType

class AddActionToStateCommand(val actionType: ActionType, val stateName: String) {

}
