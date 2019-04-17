package domain.guard

import domain.action.Action
import domain.condition.Condition
import domain.exception.DomainError
import domain.{Element, Environment}

class Guard(name: String,
            var actions: List[Action] = List(),
            var conditions: List[Condition] = List()
           ) extends Element(name) {

  def this() = this(Environment.generateUniqueName("guard"))

  def addAction(action: Action): Either[DomainError, _] = {
    Element.addElementToList(action, actions) match {
      case Left(error) => Left(error)
      case Right(modifiedActionList) =>
        actions = modifiedActionList
        (action.name :: action.getChildrenNames).foreach(Environment.addName)
        Right(())
    }
  }

  def removeAction(action: Action): Either[DomainError, _] = {
    Element.removeElementFromList(action, actions) match {
      case Left(error) => Left(error)
      case Right(modifiedActionList) =>
        actions = modifiedActionList
        (action.name :: action.getChildrenNames).foreach(Environment.removeName)
        Right(())
    }
  }

  def addCondition(condition: Condition): Either[DomainError, _] = {
    Element.addElementToList(condition, conditions) match {
      case Left(error) => Left(error)
      case Right(modifiedConditionList) =>
        conditions = modifiedConditionList
        Environment.addName(condition.name)
        Right(())
    }
  }

  def removeCondition(condition: Condition): Either[DomainError, _] = {
    Element.removeElementFromList(condition, conditions) match {
      case Left(error) => Left(error)
      case Right(modifiedConditionList) =>
        conditions = modifiedConditionList
        Environment.removeName(condition.name)
        Right(())
    }
  }

  def getChildrenNames: List[String] = actions.flatMap(a => a.name :: a.getChildrenNames) ::: conditions.map(_.name)
}
