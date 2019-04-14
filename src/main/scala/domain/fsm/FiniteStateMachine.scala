package domain.fsm

import domain.state.State

class FiniteStateMachine(id: String, var isStartDefined: Boolean, var states: List[State]) {
}
