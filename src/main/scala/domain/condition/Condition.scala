package domain.condition

import domain.Element

class Condition(name: String,
                x: Double,
                y: Double,
                var query: String = ""
               ) extends Element(name, x, y) {

}
