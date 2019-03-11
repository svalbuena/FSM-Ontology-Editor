package infrastructure.drawingpane.shape

import javafx.scene.layout.Pane
import javafx.scene.shape.Line

class Transition(var node1: ConnectableNode, var node2: ConnectableNode) extends Pane {
  private def node1Bounds = node1.getBoundsInParent
  private def node2Bounds = node2.getBoundsInParent

  private val line = new Line(node1Bounds.getCenterX, node1Bounds.getCenterY, node2Bounds.getCenterX, node2Bounds.getCenterY)

  getChildren.add(line)

  def redraw(): Unit = {
    val (startX, startY) = (node1Bounds.getCenterX, node1Bounds.getCenterY)
    val (endX, endY) = (node2Bounds.getCenterX, node2Bounds.getCenterY)

    line.setStartX(startX)
    line.setStartY(startY)
    line.setEndX(endX)
    line.setEndY(endY)
  }
}
