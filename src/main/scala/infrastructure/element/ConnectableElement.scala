package infrastructure.element

import infrastructure.element.transition.Transition

/**
  * Connectable element, for elements that can be connected with transitions
  *
  * @param name name of the connectable element
  */
abstract class ConnectableElement(name: String) extends Element(name) {
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
