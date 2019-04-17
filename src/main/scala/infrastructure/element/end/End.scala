package infrastructure.element.end

import infrastructure.drawingpane.shape.EndShape
import infrastructure.element.ConnectableElement

class End(name: String) extends ConnectableElement(name) {
  val shape = new EndShape()
}
