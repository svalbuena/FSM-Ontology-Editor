package infrastructure.drawingpane.usecase.connectablenode

import infrastructure.drawingpane.DrawingPane
import infrastructure.drawingpane.shape.ConnectableNode

class EraseConnectableNodeUseCase(drawingPane: DrawingPane) {
  def erase(connectableNode: ConnectableNode): Unit = {
    //Retrieve the transitions of the node
    connectableNode.transitions.foreach(transition => {
      val otherNode = {
        if (connectableNode != transition.node1)
          transition.node1
        else
          transition.node2
      }

      otherNode.removeTransition(transition)

      drawingPane.getChildren.remove(transition)
    })

    drawingPane.getChildren.remove(connectableNode)
  }
}
