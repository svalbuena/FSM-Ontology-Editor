package application.command.action.modify

import application.command.action.modify.ActionType.ActionType

/**
  *
  * @param actionName name of the action
  * @param actionType new type of the action
  */
class ModifyActionTypeCommand(val actionName: String, val actionType: ActionType) {

}
