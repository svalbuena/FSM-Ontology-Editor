package domain

import domain.exception.DomainError

abstract class PositionableElement(name: String,
                                   var x: Double,
                                   var y: Double
                                  ) extends Element(name) {
}
