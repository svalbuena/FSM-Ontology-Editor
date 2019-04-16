package domain.state

import domain.Element
import domain.action.Action
import domain.state.StateType.StateType

class State(name: String, x: Double, y: Double, var stateType: StateType, var actions: List[Action]) extends Element(name, x, y) {

}
