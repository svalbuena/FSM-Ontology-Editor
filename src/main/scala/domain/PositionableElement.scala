package domain

import domain.exception.DomainError

abstract class PositionableElement(name: String,
                                   private var _x: Double,
                                   private var _y: Double
                                  ) extends Element(name) {

  def x: Double = _x

  def x_=(newX: Double): Either[DomainError, Double] = {
    x = newX
    Right(x)
  }

  def y: Double = _y

  def y_=(newY: Double): Either[DomainError, Double] = {
    y = newY
    Right(y)
  }
}
