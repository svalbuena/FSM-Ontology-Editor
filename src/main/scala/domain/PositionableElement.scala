package domain

/**
  * An element that has a position on screen
  *
  * @param name name of the element
  * @param x    x coordinate of the element
  * @param y    y coordinate of the element
  */
abstract class PositionableElement(name: String,
                                   var x: Double,
                                   var y: Double,
                                   environment: Environment
                                  ) extends Element(name, environment) {
}
