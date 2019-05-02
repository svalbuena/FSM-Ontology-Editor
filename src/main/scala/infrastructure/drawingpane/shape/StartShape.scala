package infrastructure.drawingpane.shape

import javafx.scene.layout.Pane
import javafx.scene.shape.Circle

/**
  * Visual element of a Start
  * @param radius radius of the start circle
  */
class StartShape(radius: Double = 20.0) extends Pane {
  private val startCircle = new Circle(radius, radius, radius)

  getChildren.add(startCircle)
}
