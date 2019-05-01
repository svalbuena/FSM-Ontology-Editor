package application.command.state.modify

import application.command.state.modify.StateType.StateType

/**
  *
  * @param stateName name of the state
  * @param stateType new type of the state
  */
class ModifyStateTypeCommand(val stateName: String, val stateType: StateType) {

}
