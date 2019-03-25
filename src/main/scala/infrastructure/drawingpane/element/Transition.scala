package infrastructure.drawingpane.element

import javafx.scene.layout.Pane
import javafx.scene.shape.Line

class Transition(var node1: ConnectableNode, var node2: ConnectableNode) extends Pane {
  private val line = new Line()

  computeCoordinates()

  getChildren.add(line)

  def computeCoordinates(): Unit = {
    val (node1Bounds, node2Bounds) = (node1.getBoundsInParent, node2.getBoundsInParent)

    val (startX, startY) = (node1Bounds.getCenterX, node1Bounds.getCenterY)
    val (endX, endY) = (node2Bounds.getCenterX, node2Bounds.getCenterY)

    line.setStartX(startX)
    line.setStartY(startY)
    line.setEndX(endX)
    line.setEndY(endY)
  }
}
