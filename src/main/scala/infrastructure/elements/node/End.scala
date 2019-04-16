package infrastructure.elements.node

import infrastructure.drawingpane.shape.EndShape

class End(name: String) extends ConnectableElement(name) {
  val shape = new EndShape()
}
