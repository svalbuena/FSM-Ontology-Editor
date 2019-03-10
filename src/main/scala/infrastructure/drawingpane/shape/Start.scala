package infrastructure.drawingpane.shape

import javafx.scene.layout.Pane
import javafx.scene.shape.Circle

class Start(radius: Double) extends Pane {
  var transitionList: List[Transition] = List[Transition]()

  val startCircle = new Circle(radius, radius, radius)

  getChildren.add(startCircle)

  def addTransition(transition: Transition): Unit = {
    transitionList = transition :: transitionList
  }
}
