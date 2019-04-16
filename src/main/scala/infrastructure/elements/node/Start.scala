package infrastructure.elements.node

import infrastructure.drawingpane.shape.StartShape

class Start(name: String) extends ConnectableElement(name) {
  val shape = new StartShape()
}
