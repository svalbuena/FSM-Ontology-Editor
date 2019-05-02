package infrastructure.element.ghostnode

import infrastructure.drawingpane.shape.GhostShape
import infrastructure.element.ConnectableElement

/**
  * Ghost node data
  * @param name name of the ghost node
  */
class GhostNode(name: String) extends ConnectableElement(name) {
  val shape = new GhostShape
}
