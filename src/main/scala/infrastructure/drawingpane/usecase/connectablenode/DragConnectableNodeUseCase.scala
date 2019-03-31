package infrastructure.drawingpane.usecase.connectablenode

import infrastructure.drawingpane.DrawingPane
import infrastructure.drawingpane.shape.ConnectableShape

class DragConnectableNodeUseCase(drawingPane: DrawingPane) {
  def drag(connectableNode: ConnectableShape, deltaX: Double, deltaY: Double): Unit = {
    //TODO: maybe change they way null is checked
    Option(connectableNode.getBoundsInParent).foreach { shapeBounds =>
      val newX = connectableNode.getTranslateX + deltaX
      val newY = connectableNode.getTranslateY + deltaY
      val drawingPaneBounds = drawingPane.getLayoutBounds

      println()
      println("BoundsWidth = " + drawingPaneBounds.getWidth)
      println("BoundsHeight = " + drawingPaneBounds.getHeight)
      println("NewX = " + newX)
      println("NewY = " + newY)
      println("ShapeWidth = " + shapeBounds.getWidth)
      println("ShapeHeight = " + shapeBounds.getHeight)
      println()

      if (drawingPaneBounds.contains(newX, newY, shapeBounds.getWidth, shapeBounds.getHeight)) {
        println("Moving..")
        connectableNode.setTranslateX(newX)
        connectableNode.setTranslateY(newY)
      }
    }
  }
}
