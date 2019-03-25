package infrastructure.drawingpane.element

import javafx.scene.layout.Pane
import javafx.scene.paint.Color
import javafx.scene.shape.{Circle, Shape}

class End(radius: Double = 20.0) extends ConnectableNode {
  private val InnerRadiusProportion = 0.70

  private val innerCircle = new Circle(radius, radius, radius * InnerRadiusProportion)
  innerCircle.setFill(Color.BLACK)

  private val outerCircle = new Circle(radius, radius, radius)
  outerCircle.setFill(null)
  outerCircle.setStroke(Color.BLACK)

  private val endCircle = Shape.union(innerCircle, outerCircle)

  getChildren.add(endCircle)
}
