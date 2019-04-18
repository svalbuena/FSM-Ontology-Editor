package domain.fsm

import domain.exception.{DomainError, ElementNotFoundError, NameNotUniqueError, StartError}
import domain.state.{State, StateType}
import domain.transition.Transition
import domain.{Element, Environment}

class FiniteStateMachine(name: String,
                         var states: List[State] = List(),
                         var transitions: List[Transition] = List()
                        ) extends Element(name) {

  def this() = this(Environment.generateUniqueName("fsm"))


  def addState(state: State): Either[DomainError, _] = {
    if (Environment.isNameUnique(state.name)) {
      if ((state.stateType == StateType.INITIAL || state.stateType == StateType.INITIAL_FINAL) && hasInitialState) {
        Left(new StartError("An initial state is already defined"))
      } else {

        states = state :: states

        (state.name :: state.getChildrenNames).foreach(Environment.addName)
        Right(())
      }
    } else {
      Left(new NameNotUniqueError(s"Error -> Name '${state.name} is not unique"))
    }
  }

  def removeState(state: State): Either[DomainError, _] = {
    if (states.contains(state)) {
      states = states.filterNot(s => s == state)

      (state.name :: state.getChildrenNames).foreach(Environment.removeName)
      Right(())
    } else {
      Left(new ElementNotFoundError("State not found"))
    }
  }

  def addTransition(transition: Transition): Either[DomainError, _] = {
    if (Environment.isNameUnique(transition.name)) {
      transitions = transition :: transitions
      (transition.name :: transition.getChildrenNames).foreach(Environment.addName)
      Right(())
    } else {
      Left(new NameNotUniqueError(s"Error -> Name '${transition.name} is not unique"))
    }
  }

  def removeTransition(transition: Transition): Either[DomainError, _] = {
    if (transitions.contains(transition)) {
      transitions = transitions.filterNot(t => t == transition)
      (transition.name :: transition.getChildrenNames).foreach(Environment.removeName)
      Right(())
    } else {
      Left(new ElementNotFoundError("Transition not found"))
    }
  }

  def hasInitialState: Boolean = {
    states.map(_.stateType).contains(StateType.INITIAL) || states.map(_.stateType).contains(StateType.INITIAL_FINAL)
  }

  def getChildrenNames: List[String] = states.flatMap(_.getChildrenNames) ::: transitions.flatMap(_.getChildrenNames)
}
