package infrastructure.elements.node

import infrastructure.drawingpane.shape.StartShape

class Start(id: String) extends ConnectableElement(id) {
  val shape = new StartShape()
}
