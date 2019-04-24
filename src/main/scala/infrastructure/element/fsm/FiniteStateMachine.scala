package infrastructure.element.fsm

import infrastructure.element.Element
import infrastructure.element.state.State
import infrastructure.element.transition.Transition
import infrastructure.propertybox.fsm.FsmPropertiesBox

class FiniteStateMachine(name: String,
                         var baseUri: String,
                        ) extends Element(name) {
  private var filenameOption: Option[String] = None

  val propertiesBox = new FsmPropertiesBox

  var states: List[State] = List()
  var transitions: List[Transition] = List()

  def isFilenameDefined: Boolean = filenameOption.isDefined
  def getFilename: String = filenameOption.get
  def setFilename(filename: String): Unit = filenameOption = Some(filename)

  def addState(state: State): Unit = {
    states = state :: states
  }

  def addTransition(transition: Transition): Unit = {
    transitions = transition :: transitions
  }

  def removeState(state: State): Unit = {
    states = states.filterNot(s => s == state)
  }

  def removeTransition(transition: Transition): Unit = {
    transitions = transitions.filterNot(t => t == transition)
  }
}
