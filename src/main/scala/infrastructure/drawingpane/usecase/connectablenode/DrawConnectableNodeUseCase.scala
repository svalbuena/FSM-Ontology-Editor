package infrastructure.drawingpane.usecase.connectablenode

import infrastructure.drawingpane.DrawingPane
import infrastructure.drawingpane.shape.ConnectableShape

class DrawConnectableNodeUseCase(drawingPane: DrawingPane) {
  def draw(connectableNode: ConnectableShape, x: Double, y: Double): Unit = {
    connectableNode.setTranslateX(x)
    connectableNode.setTranslateY(y)

    drawingPane.getChildren.add(connectableNode)
  }
}
