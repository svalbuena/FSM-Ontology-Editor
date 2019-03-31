package infrastructure.drawingpane.shape

import javafx.scene.shape.Circle

class StartShape(radius: Double = 20.0) extends ConnectableShape {
  private val startCircle = new Circle(radius, radius, radius)

  getChildren.add(startCircle)
}
