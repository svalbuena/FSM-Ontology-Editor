package infrastructure.elements.node

import infrastructure.drawingpane.shape.GhostShape

class GhostElement(name: String) extends ConnectableElement(name) {
  val shape = new GhostShape
}
