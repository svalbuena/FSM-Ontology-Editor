package infrastructure.drawingpane.shape

import javafx.scene.layout.Pane
import javafx.scene.paint.Color
import javafx.scene.shape.Circle

/**
  * Visual element of an End
  *
  * @param radius radius of the element
  */
class EndShape(radius: Double = 20.0) extends Pane {
  private val InnerRadiusProportion = 0.70

  private val innerCircle = new Circle(radius, radius, radius * InnerRadiusProportion)
  innerCircle.setFill(Color.BLACK)

  private val outerCircle = new Circle(radius, radius, radius)
  outerCircle.setFill(null)
  outerCircle.setStroke(Color.BLACK)

  private val endCircle = javafx.scene.shape.Shape.union(innerCircle, outerCircle)

  getChildren.add(endCircle)
}
