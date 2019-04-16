package domain.action

import domain.Element
import domain.action.BodyType.BodyType

class Body(name: String,
           x: Double,
           y: Double,
           var bodyType: BodyType = BodyType.RDF,
           var content: String = ""
          ) extends Element(name, x, y) {

}
