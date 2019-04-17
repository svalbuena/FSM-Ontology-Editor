package infrastructure.element.start

import infrastructure.drawingpane.shape.StartShape
import infrastructure.element.ConnectableElement

class Start(name: String) extends ConnectableElement(name) {
  val shape = new StartShape()
}
