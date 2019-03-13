package infrastructure.drawingpane.shape

object GhostNode {
  def apply(): GhostNode = new GhostNode()
}

class GhostNode extends ConnectableNode

