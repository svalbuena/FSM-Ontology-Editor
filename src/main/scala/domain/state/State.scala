package domain.state

import domain.action.Action
import domain.exception.{DomainError, ElementNotFoundError, NameNotUniqueError, StartError}
import domain.state.StateType.StateType
import domain.{Environment, PositionableElement}

/**
  *
  * @param name       name of the state
  * @param x          x coordinate of the state
  * @param y          y coordinate of the state
  * @param _stateType type of the state
  * @param actions    actions of the state
  */
class State(name: String,
            x: Double,
            y: Double,
            private var _stateType: StateType = StateType.SIMPLE,
            var actions: List[Action] = List(),
            environment: Environment
           ) extends PositionableElement(name, x, y, environment) {

  def this(x: Double, y: Double, environment: Environment) = this(environment.generateUniqueName("state"), x, y, environment = environment)

  /**
    * Changes the type of the state, error if there is no fsm selected or if changing to an initial state and the fsm has already an initial state
    *
    * @param newStateType new state type
    * @return exception or the state type
    */
  def stateType_=(newStateType: StateType): Either[DomainError, StateType] = {
    val fsmOption = environment.getSelectedFsm match {
      case Left(_) => None
      case Right(fsm) => Some(fsm)
    }

    if (fsmOption.isDefined && (newStateType == StateType.INITIAL || newStateType == StateType.INITIAL_FINAL) && !(stateType == StateType.INITIAL && newStateType == StateType.INITIAL_FINAL) && fsmOption.get.hasInitialState) {
      Left(new StartError("A state is already defined as start"))
    } else {
      _stateType = newStateType
      Right(stateType)
    }
  }

  def stateType: StateType = _stateType

  /**
    * Adds an action to the state, error if the action name is not unique
    *
    * @param action action to be added
    * @return exception or nothing if successful
    */
  def addAction(action: Action): Either[DomainError, _] = {
    if (environment.isNameUnique(action.name)) {
      actions = action :: actions
      (action.name :: action.getChildrenNames).foreach(environment.addName)
      Right(())
    } else {
      Left(new NameNotUniqueError(s"Error -> Name '${action.name} is not unique"))
    }
  }

  /**
    * Removes an action from the state, error if the action doesn't belong to the state
    *
    * @param action action to be removed
    * @return exception or nothing if successful
    */
  def removeAction(action: Action): Either[DomainError, _] = {
    if (actions.contains(action)) {
      actions = actions.filterNot(a => a == action)
      (action.name :: action.getChildrenNames).foreach(environment.removeName)
      Right(())
    } else {
      Left(new ElementNotFoundError("Action not found"))
    }
  }

  /**
    *
    * @return names of the actions of the state and of their children
    */
  def getChildrenNames: List[String] = actions.flatMap(a => List(a.name) ::: a.getChildrenNames)
}
