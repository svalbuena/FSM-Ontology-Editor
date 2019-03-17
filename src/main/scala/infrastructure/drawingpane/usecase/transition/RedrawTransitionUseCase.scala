package infrastructure.drawingpane.usecase.transition

import infrastructure.drawingpane.DrawingPane
import infrastructure.drawingpane.shape.Transition

class RedrawTransitionUseCase(drawingPane: DrawingPane) {
  def redraw(transition: Transition): Unit = {
    transition.computeCoordinates()
    transition.toBack()
  }
}
