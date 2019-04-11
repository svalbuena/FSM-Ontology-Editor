package infrastructure.drawingpane

import infrastructure.drawingpane.shape.transition.TransitionShape
import javafx.scene.layout.Pane
import javafx.scene.shape.{LineTo, MoveTo}

class Canvas extends Pane {
  /* Connectable Node */
  def dragConnectableNode(connectableNode: Pane, deltaX: Double, deltaY: Double): Unit = {
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

  def drawConnectableNode(connectableNode: Pane, x: Double, y: Double): Unit = {
    connectableNode.setTranslateX(x)
    connectableNode.setTranslateY(y)

    getChildren.add(connectableNode)
  }

  def eraseConnectableNode(connectableNode: Pane): Unit = {
    getChildren.remove(connectableNode)
  }

  /* Transtion */
  def dragTransition(transition: TransitionShape, source: Pane, destination: Pane): Unit = {
    updateTransitionPosition(transition, source, destination)
    transition.toBack()
  }

  def drawTransition(transition: TransitionShape, source: Pane, destination: Pane): Unit = {
    updateTransitionPosition(transition, source, destination)
    getChildren.add(transition)
    transition.toBack()
  }

  def eraseTransition(transition: TransitionShape): Unit = {
    getChildren.remove(transition)
  }


  def updateTransitionPosition(transitionShape: TransitionShape, source: Pane, destination: Pane): Unit = {
    val line = transitionShape.line

    val (sourceBounds, destinationBounds) = (source.getBoundsInParent, destination.getBoundsInParent)

    val (startX, startY) = (sourceBounds.getCenterX, sourceBounds.getCenterY)
    val (endX, endY) = (destinationBounds.getCenterX, destinationBounds.getCenterY)

    val (aX, aY) = (endX - startX, endY - startY)

    val degrees = scala.math.toDegrees(scala.math.atan(aY / aX))
    println(s"Degrees = $degrees")

    line.setStart(startX, startY)
    line.setEnd(endX, endY)

    updateTransitionGuardGroupPosition(transitionShape)
  }

  def updateTransitionGuardGroupPosition(transitionShape: TransitionShape): Unit = {
    val line = transitionShape.line
    val guardGroup = transitionShape.guardGroup

    val (startX, startY) = line.getStart
    val (endX, endY) = line.getEnd

    val midX = startX + ((endX - startX) / 2) - guardGroup.getWidth / 2
    val midY = startY + ((endY - startY) / 2) - guardGroup.getHeight

    guardGroup.setTranslateX(midX)
    guardGroup.setTranslateY(midY)
  }
}
