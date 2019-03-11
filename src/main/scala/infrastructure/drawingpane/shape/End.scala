package infrastructure.drawingpane.shape

import javafx.scene.layout.Pane
import javafx.scene.paint.Color
import javafx.scene.shape.{Circle, Shape}

class End(radius: Double) extends Pane {
  val InnerRadiusProportion = 0.70

  var transitionList: List[Transition] = List[Transition]()

  val innerCircle = new Circle(radius, radius, radius * InnerRadiusProportion)
  innerCircle.setFill(Color.BLACK)

  val outerCircle = new Circle(radius, radius, radius)
  outerCircle.setFill(null)
  outerCircle.setStroke(Color.BLACK)

  val endCircle = Shape.union(innerCircle, outerCircle)

  getChildren.add(endCircle)

  def addTransition(transition: Transition): Unit = {
    transitionList = transition :: transitionList
  }
}
