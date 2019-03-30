package infrastructure.drawingpane.shape

import javafx.scene.layout.Pane

trait ConnectableNode extends Pane {

  var transitions: List[Transition] = List[Transition]()

  def addTransition(transition: Transition): Unit = {
    transitions = transition :: transitions
  }

  def removeTransition(transitionToDelete: Transition): Unit = {
    transitions = transitions.filterNot(transition => transition == transitionToDelete)
  }
}
