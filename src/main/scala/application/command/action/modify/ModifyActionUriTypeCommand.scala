package application.command.action.modify

import application.command.action.modify.UriType.UriType

/**
  *
  * @param actionName name of the action
  * @param uriType    new uri type of the action
  */
class ModifyActionUriTypeCommand(val actionName: String, val uriType: UriType) {

}
