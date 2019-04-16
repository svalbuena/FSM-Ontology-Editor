package domain.state

import domain.{Element, Environment, PositionableElement}
import domain.action.Action
import domain.exception.DomainError
import domain.state.StateType.StateType

class State(name: String,
            x: Double,
            y: Double,
            private var _stateType: StateType = StateType.SIMPLE,
            var actions: List[Action] = List()
           ) extends PositionableElement(name, x, y) {

  def this(x: Double, y: Double) = this(Environment.generateUniqueName("prototypeUriParameter"), x , y)

  def stateType: StateType = _stateType
  def stateType_= (newStateType: StateType): Either[DomainError, StateType] = {
    stateType = newStateType
    Right(stateType)
  }


  def addAction(action: Action): Either[DomainError, _] = {
    Element.addElementToList(action, actions) match {
      case Left(error) => Left(error)
      case Right(modifiedActionList) =>
        actions = modifiedActionList
        (action.name :: action.getChildrenNames).foreach(Environment.addName)
        Right()
    }
  }

  def removeAction(action: Action): Either[DomainError, _] = {
    Element.removeElementFromList(action, actions) match {
      case Left(error) => Left(error)
      case Right(modifiedActionList) =>
        actions = modifiedActionList
        (action.name :: action.getChildrenNames).foreach(Environment.removeName)
        Right()
    }
  }

  def getChildrenNames: List[String] = actions.flatMap(a => List(a.name) ::: a.getChildrenNames)
}
