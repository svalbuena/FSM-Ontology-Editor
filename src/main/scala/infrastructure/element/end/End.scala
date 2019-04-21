package infrastructure.element.end

import infrastructure.drawingpane.shape.EndShape
import infrastructure.element.ConnectableElement

class End(name: String,
          var x: Double,
          var y: Double
         ) extends ConnectableElement(name) {
  val shape = new EndShape()
}
