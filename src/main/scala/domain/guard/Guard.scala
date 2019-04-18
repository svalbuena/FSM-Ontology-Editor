package domain.guard

import domain.action.Action
import domain.condition.Condition
import domain.exception.{DomainError, ElementNotFoundError, NameNotUniqueError}
import domain.{Element, Environment}

class Guard(name: String,
            var actions: List[Action] = List(),
            var conditions: List[Condition] = List()
           ) extends Element(name) {

  def this() = this(Environment.generateUniqueName("guard"))

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

  def addCondition(condition: Condition): Either[DomainError, _] = {
    if (Environment.isNameUnique(condition.name)) {
      conditions = condition :: conditions
      Environment.addName(condition.name)
      Right(())
    } else {
      Left(new NameNotUniqueError(s"Error -> Name '${condition.name} is not unique"))
    }
  }

  def removeCondition(condition: Condition): Either[DomainError, _] = {
    if (conditions.contains(condition)) {
      conditions = conditions.filterNot(c => c == condition)
      Environment.removeName(condition.name)
      Right(())
    } else {
      Left(new ElementNotFoundError("Condition not found"))
    }
  }

  def getChildrenNames: List[String] = actions.flatMap(a => a.name :: a.getChildrenNames) ::: conditions.map(_.name)
}
