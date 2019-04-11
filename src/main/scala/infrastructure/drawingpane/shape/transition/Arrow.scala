package infrastructure.drawingpane.shape.transition

import javafx.scene.Group
import javafx.scene.layout.Pane
import javafx.scene.paint.Color
import javafx.scene.shape.{Line, Polygon}

import scala.math.{pow, sqrt}

class Arrow extends Pane {
  private val line = new Line()
  private val end = new Polygon()

  getChildren.addAll(line, end)

  def getStart: (Double, Double) = (line.getStartX, line.getStartY)
  def getEnd: (Double, Double) = (line.getEndX, line.getEndY)

  def setStart(x: Double, y: Double): Unit = {
    line.setStartX(x)
    line.setStartY(y)
  }

  def setEnd(x: Double, y: Double): Unit = {
    line.setEndX(x)
    line.setEndY(y)

    val (startX, startY) = (line.getStartX, line.getStartY)
    val (endX, endY) = (line.getEndX, line.getEndY)

    val (vectorX, vectorY) = (endX - startX, endY - startY)

    val offset = 40.0
    val opening = 15.0

    val (leftVertexX, leftVertexY, rightVertexX, rightVertexY) = (vectorY, vectorX) match {
      case (0.0, _) =>
        // <-
        if (endX - startX <= 0) (endX + offset, endY + opening, endX + offset, endY - opening)
        // ->
        else (endX - offset, endY - opening, endX - offset, endY + opening)

      case (_, 0.0) =>
        //^
        if (endY - startY <= 0) (endX - opening, endY + offset, endX + opening, endY + offset)
        //v
        else (endX + opening, endY - offset, endX - opening, endY - offset)

      case _ =>
        val m =  vectorY / vectorX
        val n = - m * endX + endY
        def lineY: Double => Double = lineEquation(m, n)

        val pivotX = {
          val result = lineAndCircleIntersection(m, n, endX, endY, offset)
          if (endX - startX <= 0) result._1
          else result._2
        }

        val pivotY = lineY(pivotX)

        val pM = - 1 / m
        val pN = pivotX / m + pivotY
        def pLineY: Double => Double = lineEquation(pM, pN)

        val (v1X, v2X) = lineAndCircleIntersection(pM, pN, pivotX, pivotY, opening)
        val (v1Y, v2Y) = (pLineY(v1X), pLineY(v2X))

        (v1X, v1Y, v2X, v2Y)
    }

    end.getPoints.clear()
    end.getPoints.addAll(endX, endY, leftVertexX, leftVertexY, rightVertexX, rightVertexY)
    end.setFill(Color.BLACK )
  }

  def lineAndCircleIntersection(m: Double, n: Double, a: Double, b: Double, r: Double): (Double, Double) = {
    val a1 = pow(m, 2) + 1
    val b2 = 2 * m * n - 2 * a - 2 * b * m
    val c = pow(n, 2) + pow(a, 2) + pow(b, 2) - 2 * b * n - pow(r, 2)
    solveQuadraticEquation(a1, b2, c)
  }

  def solveQuadraticEquation(a: Double, b: Double, c: Double): (Double, Double) = {
    val squaredDiscriminant = sqrt(pow(b, 2) - 4 * a * c)

    val numerator1 = - b + squaredDiscriminant
    val numerator2 = - b - squaredDiscriminant

    val denominator = 2 * a

    val x1 = numerator1 / denominator
    val x2 = numerator2 / denominator

    (x1, x2)
  }

  def lineEquation(m: Double, n: Double)(x: Double): Double = m * x + n
}
