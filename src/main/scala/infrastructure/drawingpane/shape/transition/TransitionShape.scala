package infrastructure.drawingpane.shape.transition

import infrastructure.drawingpane.shape.guard.{GuardGroupShape, GuardPane}
import javafx.scene.layout.Pane
import javafx.scene.shape.Line

class TransitionShape extends Pane {
  val line = new Line()
  val guardGroup = new GuardGroupShape()

  getChildren.addAll(line, guardGroup)


  def addTransitionGuard(guardPane: GuardPane): Unit = guardGroup.addGuard(guardPane)

  def removeTransitionGuard(guardPane: GuardPane): Unit = guardGroup.removeGuard(guardPane)
}
