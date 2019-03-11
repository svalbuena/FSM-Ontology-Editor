package infrastructure.drawingpane.shape

import javafx.scene.layout.Pane

class ConnectableNode extends Pane {
  var transitions: List[Transition] = List[Transition]()

  def addTransition(transition: Transition): Unit = {
    transitions = transition :: transitions
  }

  def removeTransition(transitionToDelete: Transition): Unit = {
    transitions = transitions.filterNot(transition => transition == transitionToDelete)
  }

  def drag(deltaX: Double, deltaY: Double): Unit = {
    val shapeBounds = getBoundsInParent

    val newX = getTranslateX + deltaX
    val newY = getTranslateY + deltaY

    if (getParent.getLayoutBounds.contains(newX, newY, shapeBounds.getWidth, shapeBounds.getHeight)) {
      setTranslateX(newX)
      setTranslateY(newY)
      transitions.foreach(transition => transition.redraw())
    }
  }
}
