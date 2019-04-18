package domain.state

import domain.action.Action
import domain.exception.{DomainError, ElementNotFoundError, NameNotUniqueError, StartError}
import domain.state.StateType.StateType
import domain.{Environment, PositionableElement}

class State(name: String,
            x: Double,
            y: Double,
            private var _stateType: StateType = StateType.SIMPLE,
            var actions: List[Action] = List()
           ) extends PositionableElement(name, x, y) {

  def this(x: Double, y: Double) = this(Environment.generateUniqueName("state"), x, y)

  def stateType: StateType = _stateType

  def stateType_=(newStateType: StateType): Either[DomainError, StateType] = {
    val fsmOption = Environment.getSelectedFsm match {
      case Left(_) => None
      case Right(fsm) => Some(fsm)
    }

    if (fsmOption.isDefined && (newStateType == StateType.INITIAL || newStateType == StateType.INITIAL_FINAL) && fsmOption.get.hasInitialState) {
      Left(new StartError("A state is already defined as start"))
    } else {
      _stateType = newStateType
      Right(stateType)
    }
  }

  def addAction(action: Action): Either[DomainError, _] = {
    if (Environment.isNameUnique(action.name)) {
      actions = action :: actions
      (action.name :: action.getChildrenNames).foreach(Environment.addName)
      Right(())
    } else {
      Left(new NameNotUniqueError(s"Error -> Name '${action.name} is not unique"))
    }
  }

  def removeAction(action: Action): Either[DomainError, _] = {
    if (actions.contains(action)) {
      actions = actions.filterNot(a => a == action)
      (action.name :: action.getChildrenNames).foreach(Environment.removeName)
      Right(())
    } else {
      Left(new ElementNotFoundError("Action not found"))
    }
  }

  def getChildrenNames: List[String] = actions.flatMap(a => List(a.name) ::: a.getChildrenNames)
}
