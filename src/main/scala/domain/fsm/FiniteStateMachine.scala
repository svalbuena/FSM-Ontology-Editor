package domain.fsm

import domain.{Element, Environment}
import domain.exception.{DomainError, EndError, StartError}
import domain.state.State
import domain.transition.Transition

class FiniteStateMachine(name: String,
                         private var _isStartDefined: Boolean = false,
                         private var _isEndDefined: Boolean = false,
                         var states: List[State] = List(),
                         var transitions: List[Transition] = List()
                        ) extends Element(name) {

  def this() = this(Environment.generateUniqueName("fsm"))

  def isStartDefined: Boolean = _isStartDefined
  def isStartDefined_= (newIsStartDefined: Boolean): Either[DomainError, Boolean] = {
    if (isStartDefined == newIsStartDefined) {
      if (isStartDefined) Left(new StartError("Start is already defined, only one instance allowed"))
      else Left(new StartError("There isn't any Start to remove"))
    }
    isStartDefined = newIsStartDefined
    Right(isStartDefined)
  }

  def isEndDefined: Boolean = _isStartDefined
  def isEndDefined_= (newIsEndDefined: Boolean): Either[DomainError, Boolean] = {
    if (isEndDefined == newIsEndDefined) {
      if (isEndDefined) Left(new EndError("End is already defined, only one instance allowed"))
      else Left(new EndError("There isn't any End to remove"))
    }

    isEndDefined = newIsEndDefined
    Right(isEndDefined)
  }

  def addState(state: State): Either[DomainError, _] = {
    Element.addElementToList(state, states) match {
      case Left(error) => Left(error)
      case Right(modifiedStateList) =>
        states = modifiedStateList
        (state.name :: state.getChildrenNames).foreach(Environment.addName)
        Right()
    }
  }

  def removeState(state: State): Either[DomainError, _] = {
    Element.removeElementFromList(state, states) match {
      case Left(error) => Left(error)
      case Right(modifiedStateList) =>
        states = modifiedStateList
        (state.name :: state.getChildrenNames).foreach(Environment.removeName)
        Right()
    }
  }

  def addTransition(transition: Transition): Either[DomainError, _] = {
    Element.addElementToList(transition, transitions) match {
      case Left(error) => Left(error)
      case Right(modifiedTransitionList) =>
        transitions = modifiedTransitionList
        (transition.name :: transition.getChildrenNames).foreach(Environment.addName)
        Right()
    }
  }

  def removeTransition(transition: Transition): Either[DomainError, _] = {
    Element.removeElementFromList(transition, transitions) match {
      case Left(error) => Left(error)
      case Right(modifiedTransitionList) =>
        transitions = modifiedTransitionList
        (transition.name :: transition.getChildrenNames).foreach(Environment.removeName)
        Right()
    }
  }

  def getChildrenNames: List[String] = states.flatMap(_.getChildrenNames) ::: transitions.flatMap(_.getChildrenNames)
}
