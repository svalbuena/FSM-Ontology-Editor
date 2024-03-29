package domain.element.guard

import domain.element.action.Action
import domain.element.condition.Condition
import domain.exception.{DomainError, ElementNotFoundError, NameNotUniqueError}
import domain.element.Element
import domain.environment.Environment

/**
  *
  * @param name       name of the guard
  * @param actions    actions of the guard
  * @param conditions conditions of the guard
  */
class Guard(name: String,
            var actions: List[Action] = List(),
            var conditions: List[Condition] = List(),
            environment: Environment
           ) extends Element(name, environment) {

  def this(environment: Environment) = this(environment.generateUniqueName("guard"), environment = environment)

  /**
    * Adds an action to the guard, error if the guard name is not unique
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
    * Removes an action from the guard, error if the action doesn't belong to the guard
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
    * Adds a condition to the guard, error if the condition name is not unique
    *
    * @param condition condition to be added
    * @return exception or nothing if successful
    */
  def addCondition(condition: Condition): Either[DomainError, _] = {
    if (environment.isNameUnique(condition.name)) {
      conditions = condition :: conditions
      environment.addName(condition.name)
      Right(())
    } else {
      Left(new NameNotUniqueError(s"Error -> Name '${condition.name} is not unique"))
    }
  }

  /**
    * Removes a condition from the guard
    *
    * @param condition condition to be added
    * @return exception or nothing if successful
    */
  def removeCondition(condition: Condition): Either[DomainError, _] = {
    if (conditions.contains(condition)) {
      conditions = conditions.filterNot(c => c == condition)
      environment.removeName(condition.name)
      Right(())
    } else {
      Left(new ElementNotFoundError("Condition not found"))
    }
  }

  /**
    *
    * @return the names of the conditions and actions of the guard and of their children
    */
  def getChildrenNames: List[String] = actions.flatMap(a => a.name :: a.getChildrenNames) ::: conditions.map(_.name)
}
