package domain.element.fsm

import domain.exception.{DomainError, ElementNotFoundError, NameNotUniqueError, StartError}
import domain.element.state.{State, StateType}
import domain.element.transition.Transition
import domain.element.Element
import domain.environment.Environment

/**
  *
  * @param name        name of the fsm
  * @param _baseUri    base uri of the fsm
  * @param states      list of the fsm's states
  * @param transitions list of the fsm's transitions
  */
class FiniteStateMachine(name: String,
                         var _baseUri: String,
                         var states: List[State] = List(),
                         var transitions: List[Transition] = List(),
                         environment: Environment
                        ) extends Element(name, environment) {

  def this(environment: Environment) = this(environment.generateUniqueName("fsm"), "https://www.example.com/myFsmDemo#", environment = environment)

  def baseUri: String = _baseUri

  /**
    * Sets the new base uri of the fsm, error if it is empty or blank
    *
    * @param newBaseUri new base uri
    * @return exception or the base uri
    */
  def baseUri_=(newBaseUri: String): Either[DomainError, String] = {
    if (newBaseUri.isEmpty || newBaseUri.isBlank) {
      Left(new DomainError("Base URI can't be empty or blank"))
    } else {
      _baseUri = newBaseUri
      Right(newBaseUri)
    }
  }

  /**
    * Adds a state to the fsm, error if the name of the state is not unique or if the state is of initial type and there is already an initial state
    *
    * @param state the state to add to the fsm
    * @return exception or nothing if successful
    */
  def addState(state: State): Either[DomainError, _] = {
    if (environment.isNameUnique(state.name)) {
      if ((state.stateType == StateType.INITIAL || state.stateType == StateType.INITIAL_FINAL) && hasInitialState) {
        Left(new StartError("An initial state is already defined"))
      } else {

        states = state :: states

        (state.name :: state.getChildrenNames).foreach(environment.addName)
        Right(())
      }
    } else {
      Left(new NameNotUniqueError(s"Error -> Name '${state.name} is not unique"))
    }
  }

  /**
    *
    * @return true if an initial state exists on the fsm, false otherwise
    */
  def hasInitialState: Boolean = {
    states.map(_.stateType).contains(StateType.INITIAL) || states.map(_.stateType).contains(StateType.INITIAL_FINAL)
  }

  /**
    * Removes a state from the fsm, error if the state doesn't belong to the fsm
    *
    * @param state state to be removed
    * @return exception or nothing if successful
    */
  def removeState(state: State): Either[DomainError, _] = {
    if (states.contains(state)) {
      states = states.filterNot(s => s == state)

      (state.name :: state.getChildrenNames).foreach(environment.removeName)
      Right(())
    } else {
      Left(new ElementNotFoundError("State not found"))
    }
  }

  /**
    * Adds a transition to the fsm, error if the transition name is not unique
    *
    * @param transition transition to add
    * @return exception or nothing if successful
    */
  def addTransition(transition: Transition): Either[DomainError, _] = {
    if (environment.isNameUnique(transition.name)) {
      transitions = transition :: transitions
      (transition.name :: transition.getChildrenNames).foreach(environment.addName)
      Right(())
    } else {
      Left(new NameNotUniqueError(s"Error -> Name '${transition.name} is not unique"))
    }
  }

  /**
    * Removes a transition from the fsm, error if the transition doesn't belong to the fsm
    *
    * @param transition transition to remove
    * @return exception or nothing if successful
    */
  def removeTransition(transition: Transition): Either[DomainError, _] = {
    if (transitions.contains(transition)) {
      transitions = transitions.filterNot(t => t == transition)
      (transition.name :: transition.getChildrenNames).foreach(environment.removeName)
      Right(())
    } else {
      Left(new ElementNotFoundError("Transition not found"))
    }
  }

  /**
    *
    * @return all the names of the states and transitions of the fsm
    */
  def getChildrenNames: List[String] = states.map(_.name) ::: states.flatMap(_.getChildrenNames) ::: transitions.map(_.name) ::: transitions.flatMap(_.getChildrenNames)
}
