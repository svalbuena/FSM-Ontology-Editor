package infrastructure.drawingpane.shape.guard

import javafx.scene.layout.VBox

class GuardGroupShape extends VBox {
  def addGuard(guardPane: GuardPane): Unit = getChildren.add(guardPane)

  def removeGuard(guardPane: GuardPane): Unit = getChildren.remove(guardPane)
}
