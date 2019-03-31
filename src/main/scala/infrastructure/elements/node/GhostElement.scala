package infrastructure.elements.node

import infrastructure.drawingpane.shape.GhostShape

class GhostElement(id: String) extends ConnectableElement(id) {
  val shape = new GhostShape
}
