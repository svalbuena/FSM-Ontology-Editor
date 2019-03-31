package infrastructure.drawingpane.usecase.connectablenode

import infrastructure.drawingpane.DrawingPane
import infrastructure.drawingpane.shape.ConnectableShape

class DragConnectableNodeUseCase(drawingPane: DrawingPane) {
  def drag(connectableNode: ConnectableShape, deltaX: Double, deltaY: Double): Unit = {
    //TODO: maybe change they way null is checked
    Option(connectableNode.getBoundsInParent).foreach { shapeBounds =>
      val newX = connectableNode.getTranslateX + deltaX
      val newY = connectableNode.getTranslateY + deltaY
      val parentBounds = connectableNode.getParent.getLayoutBounds

      if (parentBounds.contains(newX, newY, shapeBounds.getWidth, shapeBounds.getHeight)) {
        connectableNode.setTranslateX(newX)
        connectableNode.setTranslateY(newY)
      }
    }
  }
}
