package infrastructure.element.start

import infrastructure.drawingpane.shape.StartShape
import infrastructure.element.ConnectableElement

/**
  * Start data
  *
  * @param name name of the start
  * @param x    x coordinate of the start
  * @param y    y coordinate of the start
  */
class Start(name: String,
            var x: Double,
            var y: Double
           ) extends ConnectableElement(name) {
  val shape = new StartShape()
}
