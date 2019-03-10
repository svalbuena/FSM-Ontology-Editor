package infrastructure.drawingpane.shape

import infrastructure.drawingpane.DrawingPane.{state1, state2}
import javafx.scene.layout.Pane
import javafx.scene.shape.Line

class Transition(val state1: State, val state2: State) extends Pane {
  val state1Bounds = state1.getBoundsInParent
  val state2Bounds = state2.getBoundsInParent

  val line = new Line(state1Bounds.getCenterX, state1Bounds.getCenterY, state2Bounds.getCenterX, state2Bounds.getCenterY)

  getChildren.add(line)

  def moveLine(startX: Double, startY: Double, endX: Double, endY: Double): Unit = {
    line.setStartX(startX)
    line.setStartY(startY)
    line.setEndX(endX)
    line.setEndY(endY)
  }
}
