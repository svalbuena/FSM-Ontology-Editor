package infrastructure.drawingpane.shape

import javafx.scene.shape.Circle

class Start(radius: Double = 20.0) extends ConnectableNode {
  private val startCircle = new Circle(radius, radius, radius)

  getChildren.add(startCircle)
}
