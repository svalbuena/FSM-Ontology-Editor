package domain.element.transition

import domain.exception.{DomainError, ElementNotFoundError, NameNotUniqueError}
import domain.element.guard.Guard
import domain.element.state.State
import domain.element.Element
import domain.environment.Environment

/**
  *
  * @param name        name of the transition
  * @param source      source state of the transition
  * @param destination destination state of the transition
  * @param guards      guards of the transition
  */
class Transition(name: String,
                 val source: State,
                 val destination: State,
                 var guards: List[Guard] = List(),
                 environment: Environment
                ) extends Element(name, environment) {


  def this(source: State, destination: State, environment: Environment) = this(environment.generateUniqueName("transition"), source, destination, environment = environment)

  /**
    * Adds a guard to the transition, error if the guard name is not unique
    *
    * @param guard guard to be added
    * @return exception or nothing if successful
    */
  def addGuard(guard: Guard): Either[DomainError, _] = {
    if (environment.isNameUnique(guard.name)) {
      guards = guard :: guards
      (guard.name :: guard.getChildrenNames).foreach(environment.addName)
      Right(())
    } else {
      Left(new NameNotUniqueError(s"Error -> Name '${guard.name} is not unique"))
    }
  }

  /**
    * Removes a guard from the transition, error if the guard doesn't belong to the transition
    *
    * @param guard guard to be removed
    * @return exception or nothing if successful
    */
  def removeGuard(guard: Guard): Either[DomainError, _] = {
    if (guards.contains(guard)) {
      guards = guards.filterNot(g => g == guard)
      (guard.name :: guard.getChildrenNames).foreach(environment.removeName)
      Right(())
    } else {
      Left(new ElementNotFoundError("Guard not found"))
    }
  }

  /**
    *
    * @return the names of the guards of the transition and its children
    */
  def getChildrenNames: List[String] = guards.flatMap(g => g.name :: g.getChildrenNames)
}
