package infrastructure.drawingpane.usecase.transition

import infrastructure.drawingpane.DrawingPane
import infrastructure.drawingpane.shape.Transition

class DrawTransitionUseCase(drawingPane: DrawingPane) {
  def draw(transition: Transition): Unit = {
    drawingPane.getChildren.add(transition)
    transition.toBack()
  }
}
