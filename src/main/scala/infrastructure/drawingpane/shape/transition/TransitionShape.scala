package infrastructure.drawingpane.shape.transition

import infrastructure.drawingpane.shape.Shape
import infrastructure.drawingpane.shape.guard.{GuardGroupShape, GuardPane}
import javafx.scene.layout.VBox
import javafx.scene.shape.Line

class TransitionShape extends Shape {
  private val line = new Line()
  private val guardGroup = new GuardGroupShape()

  private val transitionPane = new VBox()
  transitionPane.getChildren.addAll(line, guardGroup)

  getChildren.add(transitionPane)


  def setCoordinates(source: Shape, destination: Shape): Unit = {
    val (sourceBounds, destinationBounds) = (source.getBoundsInParent, destination.getBoundsInParent)

    val (startX, startY) = (sourceBounds.getCenterX, sourceBounds.getCenterY)
    val (endX, endY) = (destinationBounds.getCenterX, destinationBounds.getCenterY)

    line.setStartX(startX)
    line.setStartY(startY)
    line.setEndX(endX)
    line.setEndY(endY)
  }

  def addTransitionGuard(guardPane: GuardPane): Unit = guardGroup.addGuard(guardPane)
  def removeTransitionGuard(guardPane: GuardPane): Unit = guardGroup.removeGuard(guardPane)
}
