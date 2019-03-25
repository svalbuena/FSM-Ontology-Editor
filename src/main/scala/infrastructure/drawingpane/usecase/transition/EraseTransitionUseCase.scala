package infrastructure.drawingpane.usecase.transition

import infrastructure.drawingpane.DrawingPane
import infrastructure.drawingpane.element.Transition

class EraseTransitionUseCase(drawingPane: DrawingPane) {
  def erase(transition: Transition): Unit = {
    drawingPane.getChildren.remove(transition)
  }
}
