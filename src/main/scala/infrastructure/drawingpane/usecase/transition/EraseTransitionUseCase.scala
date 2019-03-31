package infrastructure.drawingpane.usecase.transition

import infrastructure.drawingpane.DrawingPane
import infrastructure.drawingpane.shape.TransitionShape

class EraseTransitionUseCase(drawingPane: DrawingPane) {
  def erase(transition: TransitionShape): Unit = {
    drawingPane.getChildren.remove(transition)
  }
}
