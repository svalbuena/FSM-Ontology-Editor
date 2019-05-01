package infrastructure.drawingpane.shape.transition

import infrastructure.drawingpane.shape.guard.{GuardGroupShape, GuardPane}
import javafx.geometry.Point2D
import javafx.scene.Group

class TransitionShape extends Group {
  val line = new Arrow()
  val guardGroup = new GuardGroupShape()

  getChildren.addAll(line, guardGroup)


  def addTransitionGuard(guardPane: GuardPane): Unit = guardGroup.addGuard(guardPane)

  def removeTransitionGuard(guardPane: GuardPane): Unit = guardGroup.removeGuard(guardPane)

  def setPosition(srcCenter: Point2D, end: Point2D, endCenter: Point2D): Unit = {
    line.setStart(srcCenter)
    line.setEnd(end)

    val midX = srcCenter.getX + ((endCenter.getX - srcCenter.getX) / 2) - guardGroup.getWidth / 2
    val midY = srcCenter.getY + ((endCenter.getY - srcCenter.getY) / 2) - guardGroup.getHeight

    guardGroup.setTranslateX(midX)
    guardGroup.setTranslateY(midY)
  }
}
