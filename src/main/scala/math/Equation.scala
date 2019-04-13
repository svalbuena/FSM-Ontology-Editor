package math

import javafx.geometry.Point2D

import scala.math.{pow, sqrt}

object Equation {
  def lineAndCircleIntersection(lineM: Double, lineN: Double, circleCenter: Point2D, circleRadius: Double): (Point2D, Point2D) = {
    val (x1, y1, x2, y2) = {
      if (isDoubleInfinity(lineM)) {
        val x = circleCenter.getX
        val ys = circleEquation(circleCenter, circleRadius)(x)
        (x, ys._1, x, ys._2)
      } else {
        val a = pow(lineM, 2) + 1
        val b = 2 * lineM * lineN - 2 * circleCenter.getX - 2 * circleCenter.getY * lineM
        val c = pow(lineN, 2) + pow(circleCenter.getX, 2) + pow(circleCenter.getY, 2) - 2 * circleCenter.getY * lineN - pow(circleRadius, 2)

        val xs = solveQuadraticEquation(a, b, c)
        def lineY: Double => Double = lineEquation(lineM, lineN)

        val ys = (lineY(xs._1), lineY(xs._2))

        (xs._1, ys._1, xs._2, ys._2)
      }
    }

    (new Point2D(x1, y1), new Point2D(x2, y2))
  }

  def solveQuadraticEquation(a: Double, b: Double, c: Double): (Double, Double) = {
    val squaredDiscriminant = sqrt(pow(b, 2) - 4 * a * c)

    val numerator1 = -b + squaredDiscriminant
    val numerator2 = -b - squaredDiscriminant

    val denominator = 2 * a

    val x1 = numerator1 / denominator
    val x2 = numerator2 / denominator

    (x1, x2)
  }

  def lineEquation(m: Double, n: Double)(x: Double): Double = m * x + n

  def circleEquation(center: Point2D, radius: Double)(x: Double): (Double, Double) = {
    val a = 1
    val b = -2 * center.getY
    val c = pow(x, 2) - 2 * center.getX * x + pow(center.getX, 2) + pow(center.getY, 2) - pow(radius, 2)

    solveQuadraticEquation(a, b, c)
  }


  def lineAndLineIntersection(start1: Point2D, end1: Point2D, start2: Point2D, end2: Point2D): Point2D = {
    val (m1, n1) =  getLineEquationParameters(start1, end1)
    val (m2, n2) =  getLineEquationParameters(start2, end2)


    val (x, y) = {
      if (isDoubleInfinity(m1) && isDoubleInfinity(m2)) (Double.NaN, Double.NaN)
      else if (isDoubleInfinity(m1)) (start1.getX, lineEquation(m2, n2)(start1.getX))
      else if (isDoubleInfinity(m2)) (start2.getX, lineEquation(m1, n1)(start2.getX))
      else {
        val x = (n2 - n1) / (m1 - m2)
        val y = lineEquation(m1, n1)(x)
        (x, y)
      }
    }

    new Point2D(x, y)
  }



  def getLineEquationParameters(start: Point2D, end: Point2D): (Double, Double) = {
    val (vectorX, vectorY) = (end.getX - start.getX, end.getY - start.getY)

    val m = vectorY / vectorX
    val n = - m * end.getX + end.getY

    (m, n)
  }

  def getPerpendicularLineEquationParameters(m: Double, n: Double, point: Point2D): (Double, Double) = {
    val pM = -1 / m
    val pN = point.getX / m + point.getY

    (pM, pN)
  }

  def isPointBetweenPoints(point: Point2D, start: Point2D, end: Point2D): Boolean = {
    isDoubleBetweenDoubles(point.getX, start.getX, end.getX) && isDoubleBetweenDoubles(point.getY, start.getY, end.getY)
  }

  def isDoubleBetweenDoubles(double: Double, start: Double, end: Double): Boolean = {
    if (end >= start) double >= start && double <= end
    else double>= end && double <= start
  }

  private def isDoubleInfinity(double: Double): Boolean = double == Double.NegativeInfinity || double == Double.PositiveInfinity
}
