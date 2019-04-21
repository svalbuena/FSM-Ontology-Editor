package infrastructure.element.start

import infrastructure.drawingpane.shape.StartShape
import infrastructure.element.ConnectableElement

class Start(name: String,
            var x: Double,
            var y: Double
           ) extends ConnectableElement(name) {
  val shape = new StartShape()
}
