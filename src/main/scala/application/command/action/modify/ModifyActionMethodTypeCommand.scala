package application.command.action.modify

import application.command.action.modify.MethodType.MethodType

/**
  *
  * @param actionName name of the action
  * @param method     new method of the action
  */
class ModifyActionMethodTypeCommand(val actionName: String, val method: MethodType) {

}
