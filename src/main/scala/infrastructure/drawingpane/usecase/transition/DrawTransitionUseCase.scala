package infrastructure.drawingpane.usecase.transition

import infrastructure.drawingpane.DrawingPane
import infrastructure.drawingpane.shape.{Shape, TransitionShape}

class DrawTransitionUseCase(drawingPane: DrawingPane) {
  def draw(transition: TransitionShape, source: Shape, destination: Shape): Unit = {
    transition.setCoordinates(source, destination)
    drawingPane.getChildren.add(transition)
    transition.toBack()
  }
}
