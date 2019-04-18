package infrastructure.drawingpane.shape.transition

import infrastructure.drawingpane.shape.guard.{GuardGroupShape, GuardPane}
import javafx.geometry.Point2D
import javafx.scene.Group

class TransitionShape extends Group {
  val line = new Arrow()
  val guardGroup = new GuardGroupShape()

  setStyle("-fx-background-color: #b3c6b3")

  getChildren.addAll(line, guardGroup)


  def addTransitionGuard(guardPane: GuardPane): Unit = guardGroup.addGuard(guardPane)

  def removeTransitionGuard(guardPane: GuardPane): Unit = guardGroup.removeGuard(guardPane)

  def setPosition(start: Point2D, end: Point2D): Unit = {
    line.setStart(start)
    line.setEnd(end)

    val midX = start.getX + ((end.getX - start.getX) / 2) - guardGroup.getWidth / 2
    val midY = start.getY + ((end.getY - start.getY) / 2) - guardGroup.getHeight

    guardGroup.setTranslateX(midX)
    guardGroup.setTranslateY(midY)
  }
}
