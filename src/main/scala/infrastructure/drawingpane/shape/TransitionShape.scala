package infrastructure.drawingpane.shape

import javafx.scene.shape.Line

class TransitionShape() extends Shape {
  private val line = new Line()

  getChildren.add(line)

  def setCoordinates(source: Shape, destination: Shape): Unit = {
    val (sourceBounds, destinationBounds) = (source.getBoundsInParent, destination.getBoundsInParent)

    val (startX, startY) = (sourceBounds.getCenterX, sourceBounds.getCenterY)
    val (endX, endY) = (destinationBounds.getCenterX, destinationBounds.getCenterY)

    line.setStartX(startX)
    line.setStartY(startY)
    line.setEndX(endX)
    line.setEndY(endY)
  }
}
