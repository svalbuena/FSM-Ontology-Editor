package infrastructure.element.end

import infrastructure.drawingpane.shape.EndShape
import infrastructure.element.ConnectableElement

/**
  * End data
  * @param name name of the data
  * @param x x coordinate of the end
  * @param y y coordinate of the end
  */
class End(name: String,
          var x: Double,
          var y: Double
         ) extends ConnectableElement(name) {
  val shape = new EndShape()
}
