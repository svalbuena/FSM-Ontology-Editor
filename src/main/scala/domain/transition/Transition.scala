package domain.transition

import domain.Element
import domain.guard.Guard
import domain.state.State

class Transition(name: String,
                 x: Double,
                 y: Double,
                 val source: State,
                 val destination: State,
                 var guards: List[Guard] = List()
                ) extends Element(name, x, y) {

}
