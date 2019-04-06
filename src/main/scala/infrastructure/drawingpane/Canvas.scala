package infrastructure.drawingpane

import infrastructure.drawingpane.shape.{ConnectableShape, Shape, TransitionShape}
import javafx.scene.layout.Pane

class Canvas extends Pane {
  /* Connectable Node */
  def dragConnectableNode(connectableNode: ConnectableShape, deltaX: Double, deltaY: Double): Unit = {
    //TODO: maybe change they way null is checked
    Option(connectableNode.getBoundsInParent).foreach { shapeBounds =>
      val newX = connectableNode.getTranslateX + deltaX
      val newY = connectableNode.getTranslateY + deltaY
      val drawingPaneBounds = getLayoutBounds

      if (drawingPaneBounds.contains(newX, newY, shapeBounds.getWidth, shapeBounds.getHeight)) {
        connectableNode.setTranslateX(newX)
        connectableNode.setTranslateY(newY)
      }
    }
  }

  def drawConnectableNode(connectableNode: ConnectableShape, x: Double, y: Double): Unit = {
    connectableNode.setTranslateX(x)
    connectableNode.setTranslateY(y)

    getChildren.add(connectableNode)
  }

  def eraseConnectableNode(connectableNode: ConnectableShape): Unit = {
    getChildren.remove(connectableNode)
  }

  /* Transtion */
  def dragTransition(transition: TransitionShape, source: Shape, destination: Shape): Unit = {
    transition.setCoordinates(source, destination)
    transition.toBack()
  }

  def drawTransition(transition: TransitionShape, source: Shape, destination: Shape): Unit = {
    transition.setCoordinates(source, destination)
    getChildren.add(transition)
    transition.toBack()
  }

  def eraseTransition(transition: TransitionShape): Unit = {
    getChildren.remove(transition)
  }
}
