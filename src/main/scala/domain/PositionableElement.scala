package domain

abstract class PositionableElement(name: String,
                                   var x: Double,
                                   var y: Double
                                  ) extends Element(name) {
}
