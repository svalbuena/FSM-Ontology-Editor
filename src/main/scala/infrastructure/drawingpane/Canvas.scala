package infrastructure.drawingpane

import infrastructure.drawingpane.shape.transition.TransitionShape
import infrastructure.math.Equation
import javafx.geometry.Point2D
import javafx.scene.layout.Pane

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
    getChildren.add(connectableNode)

    connectableNode.setTranslateX(x)
    connectableNode.setTranslateY(y)
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
    getChildren.add(transition)
    updateTransitionPosition(transition, source, destination)
    transition.toBack()
  }

  def eraseTransition(transition: TransitionShape): Unit = {
    getChildren.remove(transition)
  }

  def updateTransitionPosition(transitionShape: TransitionShape, src: Pane, dst: Pane): Unit = {
    layout()

    val line = transitionShape.line

    val (srcWidth, srcHeight) = (src.getWidth, src.getHeight)
    val (dstWidth, dstHeight) = (dst.getWidth, dst.getHeight)

    println(s"Src dimensions = $srcWidth, $srcHeight")
    println(s"Src translate = ${src.getTranslateX}, ${src.getTranslateY}")

    println(s"Dst dimensions = $dstWidth, $dstHeight")


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

    line.setStart(srcCenter)
    line.setEnd(end)

    updateTransitionGuardGroupPosition(transitionShape)
  }

  def updateTransitionGuardGroupPosition(transitionShape: TransitionShape): Unit = {
    val line = transitionShape.line
    val guardGroup = transitionShape.guardGroup

    guardGroup.layout()
    layout()

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
