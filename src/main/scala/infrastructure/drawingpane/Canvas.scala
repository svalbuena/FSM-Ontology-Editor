package infrastructure.drawingpane

import infrastructure.drawingpane.shape.transition.TransitionShape
import infrastructure.math.Equation
import javafx.geometry.Point2D
import javafx.scene.Node
import javafx.scene.layout.Pane

class Canvas extends Pane {
  getStyleClass.add("canvas")

  /* Connectable Node */
  def moveNode(node: Node, deltaX: Double, deltaY: Double): Point2D = {
    val newX = node.getTranslateX + deltaX
    val newY = node.getTranslateY + deltaY

    if (getLayoutBounds.contains(newX, newY, node.getLayoutBounds.getWidth, node.getLayoutBounds.getHeight)) {
      node.setTranslateX(newX)
      node.setTranslateY(newY)
      true
    }

    new Point2D(newX, newY)
  }

  def drawNode(node: Node, x: Double, y: Double): Unit = {
    node.setTranslateX(x)
    node.setTranslateY(y)

    getChildren.add(node)
  }

  /* Transtion */
  def moveTransition(transition: TransitionShape, source: Node, destination: Node): Unit = {
    updateTransitionPosition(transition, source, destination)
    transition.toBack()
  }

  def drawTransition(transition: TransitionShape, source: Node, destination: Node): Unit = {
    getChildren.add(transition)
    moveTransition(transition, source, destination)
  }

  def updateTransitionPosition(transitionShape: TransitionShape, src: Node, dst: Node): Unit = {
    layout()

    val (srcWidth, srcHeight) = (src.getLayoutBounds.getWidth, src.getLayoutBounds.getHeight)
    val (dstWidth, dstHeight) = (dst.getLayoutBounds.getWidth, dst.getLayoutBounds.getHeight)


    val srcCenter = new Point2D(src.getTranslateX + srcWidth / 2, src.getTranslateY + srcHeight / 2)
    val dstCenter = new Point2D(dst.getTranslateX + dstWidth / 2, dst.getTranslateY + dstHeight / 2)

    val degrees = getHumanDegrees(dstCenter, srcCenter)

    val upperLeftCorner = new Point2D(dstCenter.getX - dstWidth / 2, dstCenter.getY - dstHeight / 2)
    val upperRightCorner = new Point2D(dstCenter.getX + dstWidth / 2, dstCenter.getY - dstHeight / 2)
    val lowerLeftCorner = new Point2D(dstCenter.getX - dstWidth / 2, dstCenter.getY + dstHeight / 2)
    val lowerRightCorner = new Point2D(dstCenter.getX + dstWidth / 2, dstCenter.getY + dstHeight / 2)

    val upperLeftCornerDegree = getHumanDegrees(dstCenter, upperLeftCorner)
    val upperRightCornerDegree = getHumanDegrees(dstCenter, upperRightCorner)
    val lowerLeftCornerDegree = getHumanDegrees(dstCenter, lowerLeftCorner)
    val lowerRightCornerDegree = getHumanDegrees(dstCenter, lowerRightCorner)

    val end = {
      if (degrees >= upperRightCornerDegree && degrees < upperLeftCornerDegree) Equation.lineAndLineIntersection(srcCenter, dstCenter, upperRightCorner, upperLeftCorner)
      else if (degrees >= upperLeftCornerDegree && degrees < lowerLeftCornerDegree) Equation.lineAndLineIntersection(srcCenter, dstCenter, upperLeftCorner, lowerLeftCorner)
      else if (degrees >= lowerLeftCornerDegree && degrees < lowerRightCornerDegree) Equation.lineAndLineIntersection(srcCenter, dstCenter, lowerLeftCorner, lowerRightCorner)
      else Equation.lineAndLineIntersection(srcCenter, dstCenter, lowerRightCorner, upperRightCorner)
    }

    transitionShape.setPosition(srcCenter, end, dstCenter)
  }

  private def getHumanDegrees(start: Point2D, end: Point2D): Double = {
    val (aX, aY) = (start.getX - end.getX, start.getY - end.getY)

    val radians = math.atan2(-aY, aX)

    math.toDegrees(radians) + 180
  }
}
