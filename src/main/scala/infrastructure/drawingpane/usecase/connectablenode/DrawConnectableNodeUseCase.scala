package infrastructure.drawingpane.usecase.connectablenode

import infrastructure.drawingpane.DrawingPane
import infrastructure.drawingpane.element.ConnectableNode

class DrawConnectableNodeUseCase(drawingPane: DrawingPane) {
  def draw(connectableNode: ConnectableNode, x: Double, y: Double): Unit = {
    connectableNode.setTranslateX(x)
    connectableNode.setTranslateY(y)

    drawingPane.getChildren.add(connectableNode)
  }
}
