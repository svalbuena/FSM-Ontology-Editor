package infrastructure.element.ghostnode

import infrastructure.drawingpane.shape.GhostShape
import infrastructure.element.ConnectableElement

class GhostNode(name: String) extends ConnectableElement(name) {
  val shape = new GhostShape
}
