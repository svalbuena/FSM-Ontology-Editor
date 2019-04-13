package infrastructure.drawingpane

import infrastructure.drawingpane.shape.transition.TransitionShape
import javafx.geometry.Point2D
import javafx.scene.layout.Pane
import math.Equation

import scala.math

class Canvas extends Pane {
  /* Connectable Node */
  def dragConnectableNode(connectableNode: Pane, deltaX: Double, deltaY: Double): Unit = {
    val newX = connectableNode.getTranslateX + deltaX
    val newY = connectableNode.getTranslateY + deltaY

    if (getLayoutBounds.contains(newX, newY, connectableNode.getWidth, connectableNode.getHeight)) {
      connectableNode.setTranslateX(newX)
      connectableNode.setTranslateY(newY)
    }
  }

  def drawConnectableNode(connectableNode: Pane, x: Double, y: Double): Unit = {
    connectableNode.setTranslateX(x)
    connectableNode.setTranslateY(y)

    getChildren.add(connectableNode)
  }

  def eraseConnectableNode(connectableNode: Pane): Unit = {
    getChildren.remove(connectableNode)
  }

  /* Transtion */
  def dragTransition(transition: TransitionShape, source: Pane, destination: Pane): Unit = {
    updateTransitionPosition(transition, source, destination)
    transition.toBack()
  }

  def drawTransition(transition: TransitionShape, source: Pane, destination: Pane): Unit = {
    updateTransitionPosition(transition, source, destination)
    getChildren.add(transition)
    transition.toBack()
  }

  def eraseTransition(transition: TransitionShape): Unit = {
    getChildren.remove(transition)
  }

  def updateTransitionPosition(transitionShape: TransitionShape, source: Pane, destination: Pane): Unit = {
    val line = transitionShape.line

    val (sourceBounds, destinationBounds) = (source.getBoundsInParent, destination.getBoundsInParent)
    val (width, height) = (destinationBounds.getWidth, destinationBounds.getHeight)

    val startCenter = new Point2D(sourceBounds.getCenterX, sourceBounds.getCenterY)
    val endCenter = new Point2D(destinationBounds.getCenterX, destinationBounds.getCenterY)

    val degrees = getHumanDegrees(endCenter, startCenter)

    val upperLeftCorner = new Point2D(endCenter.getX - width / 2, endCenter.getY - height / 2)
    val upperRightCorner = new Point2D(endCenter.getX + width / 2, endCenter.getY - height / 2)
    val lowerLeftCorner = new Point2D(endCenter.getX - width / 2, endCenter.getY + height / 2)
    val lowerRightCorner = new Point2D(endCenter.getX + width / 2, endCenter.getY + height / 2)

    val upperLeftCornerDegree = getHumanDegrees(endCenter, upperLeftCorner)
    val upperRightCornerDegree = getHumanDegrees(endCenter, upperRightCorner)
    val lowerLeftCornerDegree = getHumanDegrees(endCenter, lowerLeftCorner)
    val lowerRightCornerDegree = getHumanDegrees(endCenter, lowerRightCorner)

    val end = {
      if (degrees >= upperRightCornerDegree && degrees < upperLeftCornerDegree) Equation.lineAndLineIntersection(startCenter, endCenter, upperRightCorner, upperLeftCorner)
      else if (degrees >= upperLeftCornerDegree && degrees < lowerLeftCornerDegree) Equation.lineAndLineIntersection(startCenter, endCenter, upperLeftCorner, lowerLeftCorner)
      else if (degrees >= lowerLeftCornerDegree && degrees < lowerRightCornerDegree) Equation.lineAndLineIntersection(startCenter, endCenter, lowerLeftCorner, lowerRightCorner)
      else Equation.lineAndLineIntersection(startCenter, endCenter, lowerRightCorner, upperRightCorner)
    }

    line.setStart(startCenter)
    line.setEnd(end)

    updateTransitionGuardGroupPosition(transitionShape)
  }

  def updateTransitionGuardGroupPosition(transitionShape: TransitionShape): Unit = {
    val line = transitionShape.line
    val guardGroup = transitionShape.guardGroup

    val (startX, startY) = line.getStart
    val (endX, endY) = line.getEnd

    val midX = startX + ((endX - startX) / 2) - guardGroup.getWidth / 2
    val midY = startY + ((endY - startY) / 2) - guardGroup.getHeight

    guardGroup.setTranslateX(midX)
    guardGroup.setTranslateY(midY)
  }

  private def getHumanDegrees(start: Point2D, end: Point2D): Double = {
    val (aX, aY) = (start.getX - end.getX, start.getY - end.getY)

    val radians = math.atan2(-aY, aX)

    math.toDegrees(radians) + 180
  }
}
