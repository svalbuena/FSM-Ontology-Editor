package infrastructure.drawingpane.shape.guard

import infrastructure.drawingpane.shape.Shape
import javafx.scene.layout.VBox

class GuardGroupShape extends Shape {
  private val guardsPane = new VBox()

  getChildren.add(guardsPane)


  def addGuard(guardPane: GuardPane): Unit = guardsPane.getChildren.add(guardPane)
  def removeGuard(guardPane: GuardPane): Unit = guardsPane.getChildren.remove(guardPane)
}
