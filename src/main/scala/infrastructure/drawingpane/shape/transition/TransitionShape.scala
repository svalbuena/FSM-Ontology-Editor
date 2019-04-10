package infrastructure.drawingpane.shape.transition

import infrastructure.drawingpane.shape.guard.{GuardGroupShape, GuardPane}
import javafx.scene.layout.Pane

class TransitionShape extends Pane {
  val line = new Arrow()

  val guardGroup = new GuardGroupShape()

  getChildren.addAll(line, guardGroup)


  def addTransitionGuard(guardPane: GuardPane): Unit = guardGroup.addGuard(guardPane)

  def removeTransitionGuard(guardPane: GuardPane): Unit = guardGroup.removeGuard(guardPane)
}
