package infrastructure.elements.start

import infrastructure.drawingpane.shape.StartShape
import infrastructure.elements.ConnectableElement

class Start(name: String) extends ConnectableElement(name) {
  val shape = new StartShape()
}
