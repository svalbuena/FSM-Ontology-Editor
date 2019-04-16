package domain.fsm

import domain.state.State

class FiniteStateMachine(var name: String,
                         var isStartDefined: Boolean = false,
                         var isEndDefined: Boolean = false,
                         var states: List[State] = List()
                        ) {
}
