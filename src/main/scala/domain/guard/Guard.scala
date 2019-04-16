package domain.guard

import domain.Element
import domain.action.Action
import domain.condition.Condition

class Guard(name: String,
            x: Double,
            y: Double,
            var actions: List[Action] = List(),
            var conditions: List[Condition] = List()
           ) extends Element(name, x, y)  {

}
