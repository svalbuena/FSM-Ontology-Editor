package infrastructure.drawingpane.shape.transition

import javafx.scene.Group
import javafx.scene.layout.Pane
import javafx.scene.paint.Color
import javafx.scene.shape.{Line, Polygon}

import scala.math.pow

class Arrow extends Pane {
  private val line = new Line()
  private val leftEnd = new Line()
  private val rightEnd = new Line()
  private val end = new Polygon()
  end.setTranslateX(300)
  end.setTranslateY(300)

  getChildren.addAll(line, leftEnd, rightEnd)

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

    val (leftVertexX, leftVertexY, rightVertexX, rightVertexY) = (vectorX, vectorY) match {
      case (_, 0.0) => (endX - opening, endY - offset, endX + opening, endY - offset)
      case (0.0, _) => (endX - offset, endY - opening, endX - offset, endY + opening)
      case _ =>
        val m =  vectorY / vectorX
        println(s"Start -> x: ${startX} ${startY}")
        println(s"End -> x: ${endX} ${endY}")

        println(s"vX ${vectorX} ${vectorY}")
        val n = - m * endX + endY
        def lineY: Double => Double = lineEquation(m, n)
        println(s"Line -> y = ${m}x + ${n}")

        val (pivotX, _) = lineAndCircleIntersection(m, n, endX, endY, offset)
        val pivotY = m * pivotX + n

        println(s"Pivot: ${pivotX} ${pivotY}")

        val pM = - 1 / m
        val pN = pivotX / m + pivotY
        def pLineY: Double => Double = lineEquation(pM, pN)
        println(s"PLine -> y = ${pM}x + ${pN}")

        val (v1X, v2X) = lineAndCircleIntersection(pM, pN, pivotX, pivotY, opening)
        val (v1Y, v2Y) = (pLineY(v1X), pLineY(v2X))

        (v1X, v1Y, v2X, v2Y)
    }

    println(s"End -> x: $endX y: $endY")
    println(s"Lft -> x: $leftVertexX y: $leftVertexY")
    println(s"Rgt -> x: $rightVertexX y: $rightVertexY")

    end.getPoints.clear()
    //end.getPoints.addAll(endX, endY, leftVertexX, leftVertexY, rightVertexX, rightVertexY)
    end.getPoints.addAll(0.0, 0.0, 40.0, 40.0, 80.0, 80.0)
    end.setFill(Color.BLACK )

    leftEnd.setStartX(leftVertexX)
    leftEnd.setStartY(leftVertexY)
    leftEnd.setEndX(endX)
    leftEnd.setEndY(endY)

    rightEnd.setStartX(rightVertexX)
    rightEnd.setStartY(rightVertexY)
    rightEnd.setEndX(endX)
    rightEnd.setEndY(endY)
  }

  def lineAndCircleIntersection(m: Double, n: Double, a: Double, b: Double, squaredR: Double): (Double, Double) = {
    val a1 = pow(m, 2) + 1
    val b2 = 2 * m * n - 2 * a - 2 * b * n
    val c = pow(n, 2) + pow(a, 2) + pow(b, 2) - 2 * b * n - squaredR
    solveQuadraticEquation(a1, b2, c)
  }

  def solveQuadraticEquation(a: Double, b: Double, c: Double): (Double, Double) = {
    val squaredDiscriminant = pow(b * b - 4 * a * c, 2)

    val numerator1 = - b + squaredDiscriminant
    val numerator2 = - b - squaredDiscriminant

    val denominator = 2 * a

    val x1 = numerator1 / denominator
    val x2 = numerator2 / denominator

    (x1, x2)
  }

  def lineEquation(m: Double, n: Double)(x: Double): Double = m * x + n
}
