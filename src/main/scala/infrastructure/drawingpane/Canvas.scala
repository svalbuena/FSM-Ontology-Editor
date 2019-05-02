package infrastructure.drawingpane

import infrastructure.drawingpane.shape.transition.TransitionShape
import infrastructure.math.Equation
import javafx.geometry.Point2D
import javafx.scene.Node
import javafx.scene.layout.Pane

/**
  * Canvas where the elements are drawn
  */
class Canvas extends Pane {
  getStyleClass.add("canvas")

  /* Connectable Node */
  /**
    * Moves a node of the canvas, it checks
    * @param node node to be moved
    * @param deltaX deltaX of the movement
    * @param deltaY deltaY of the movement
    * @return returns the new point if it has been moved, returns the actual point otherwise
    */
  def moveNode(node: Node, deltaX: Double, deltaY: Double): Point2D = {
    val newX = node.getTranslateX + deltaX
    val newY = node.getTranslateY + deltaY

    if (getLayoutBounds.contains(newX, newY, node.getLayoutBounds.getWidth, node.getLayoutBounds.getHeight)) {
      node.setTranslateX(newX)
      node.setTranslateY(newY)

      new Point2D(newX, newY)
    } else {
      new Point2D(node.getTranslateX, node.getTranslateY)
    }
  }

  /**
    * Draws a node on the canvas
    * @param node node to be drawn
    * @param x x coordinate of the node
    * @param y y corodinate of the node
    */
  def drawNode(node: Node, x: Double, y: Double): Unit = {
    node.setTranslateX(x)
    node.setTranslateY(y)

    getChildren.add(node)
  }

  /* Transtion */
  /**
    * Moves a transition on the canvas according to the source and destination nodes position
    * @param transition transition to me moved
    * @param source source node shape
    * @param destination destination node shape
    */
  def moveTransition(transition: TransitionShape, source: Node, destination: Node): Unit = {
    updateTransitionPosition(transition, source, destination)
    transition.toBack()
  }

  /**
    * Draws a transition on the canvas
    * @param transition transition to be drawn
    * @param source source node shape
    * @param destination destination node shape
    */
  def drawTransition(transition: TransitionShape, source: Node, destination: Node): Unit = {
    getChildren.add(transition)
    moveTransition(transition, source, destination)
  }

  /**
    * Updates a transition position
    * @param transitionShape transition shape
    * @param src src shape
    * @param dst dst shape
    */
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

  /**
    * Converts a canvas degree to the degree perceived on the screen
    * @param start start point
    * @param end end point
    * @return the degree value between 0 and 360
    */
  private def getHumanDegrees(start: Point2D, end: Point2D): Double = {
    val (aX, aY) = (start.getX - end.getX, start.getY - end.getY)

    val radians = math.atan2(-aY, aX)

    math.toDegrees(radians) + 180
  }
}
