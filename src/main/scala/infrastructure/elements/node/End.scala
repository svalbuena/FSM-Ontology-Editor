package infrastructure.elements.node

import infrastructure.drawingpane.shape.EndShape

class End(id: String) extends ConnectableElement(id) {
  val shape = new EndShape()
}
