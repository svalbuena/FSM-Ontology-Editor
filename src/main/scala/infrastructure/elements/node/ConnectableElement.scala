package infrastructure.elements.node

import infrastructure.elements.Element
import infrastructure.elements.transition.Transition

abstract class ConnectableElement(id: String) extends Element(id) {
  var inTransitions: List[Transition] = List[Transition]()
  var outTransitions: List[Transition] = List[Transition]()

  def addInTransition(transition: Transition): Unit = {
    inTransitions = transition :: inTransitions
  }

  def addOutTransition(transition: Transition): Unit = {
    outTransitions = transition :: outTransitions
  }

  def getTransitions: List[Transition] = {
    inTransitions ::: outTransitions
  }

  def removeTransition(transitionToDelete: Transition): Unit = {
    inTransitions = inTransitions.filterNot(transition => transition == transitionToDelete)
    outTransitions = outTransitions.filterNot(transition => transition == transitionToDelete)
  }
}
