package infrastructure.drawingpane.usecase.connectablenode

import infrastructure.drawingpane.DrawingPane
import infrastructure.drawingpane.shape.ConnectableShape

class EraseConnectableNodeUseCase(drawingPane: DrawingPane) {
  def erase(connectableNode: ConnectableShape): Unit = {
    drawingPane.getChildren.remove(connectableNode)
  }
}
