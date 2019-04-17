package infrastructure.elements.end

import infrastructure.drawingpane.shape.EndShape
import infrastructure.elements.ConnectableElement

class End(name: String) extends ConnectableElement(name) {
  val shape = new EndShape()
}
