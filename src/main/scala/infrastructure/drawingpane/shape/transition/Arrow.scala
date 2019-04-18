package infrastructure.drawingpane.shape.transition

import infrastructure.math.Equation
import javafx.geometry.Point2D
import javafx.scene.Group
import javafx.scene.paint.Color
import javafx.scene.shape.{Line, Polygon}

class Arrow extends Group {
  private val line = new Line()
  private val edge = new Polygon()

  getChildren.addAll(line, edge)

  def getStart: Point2D = new Point2D(line.getStartX, line.getStartY)

  def getEnd: Point2D = new Point2D(line.getEndX, line.getEndY)

  def setStart(start: Point2D): Unit = {
    line.setStartX(start.getX)
    line.setStartY(start.getY)
  }

  def setEnd(end: Point2D): Unit = {
    line.setEndX(end.getX)
    line.setEndY(end.getY)

    val start = new Point2D(line.getStartX, line.getStartY)

    val offset = 40.0
    val opening = 15.0

    val (lineM, lineN) = Equation.getLineEquationParameters(start, end)

    val edgeBase = {
      val (point1, point2) = Equation.lineAndCircleIntersection(lineM, lineN, end, offset)

      if (Equation.isPointBetweenPoints(point1, start, end)) point1
      else point2
    }

    val (pM, pN) = Equation.getPerpendicularLineEquationParameters(lineM, lineN, edgeBase)
    val (leftVertex, rightVertex) = Equation.lineAndCircleIntersection(pM, pN, edgeBase, opening)

    edge.getPoints.clear()
    edge.getPoints.addAll(end.getX, end.getY, leftVertex.getX, leftVertex.getY, rightVertex.getX, rightVertex.getY)
    edge.setFill(Color.BLACK)
  }
}
