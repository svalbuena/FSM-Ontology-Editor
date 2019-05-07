package application.command.action.add

import application.command.action.modify.ActionType.ActionType


/**
  *
  * @param actionType type of the action, it has to be entry or exit
  * @param stateName  parent of the action
  */
class AddActionToStateCommand(val actionType: ActionType, val stateName: String) {

}
