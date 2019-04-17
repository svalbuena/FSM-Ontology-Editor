package infrastructure.elements.ghostnode

import infrastructure.drawingpane.shape.GhostShape
import infrastructure.elements.ConnectableElement

class GhostElement(name: String) extends ConnectableElement(name) {
  val shape = new GhostShape
}
