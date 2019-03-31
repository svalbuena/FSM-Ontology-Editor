package infrastructure.drawingpane.usecase.transition

import infrastructure.drawingpane.DrawingPane
import infrastructure.drawingpane.shape.{Shape, TransitionShape}
import javax.print.attribute.standard.Destination

class DragTransitionUseCase(drawingPane: DrawingPane) {
  def drag(transition: TransitionShape, source: Shape, destination: Shape): Unit = {
    transition.setCoordinates(source, destination)
    transition.toBack()
  }
}
