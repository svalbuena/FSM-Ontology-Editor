package domain.transition

import domain.exception.{DomainError, ElementNotFoundError, NameNotUniqueError}
import domain.guard.Guard
import domain.state.State
import domain.{Element, Environment}

class Transition(name: String,
                 val source: State,
                 val destination: State,
                 var guards: List[Guard] = List()
                ) extends Element(name) {


  def this(source: State, destination: State) = this(Environment.generateUniqueName("transition"), source, destination)

  def addGuard(guard: Guard): Either[DomainError, _] = {
    if (Environment.isNameUnique(guard.name)) {
      guards = guard :: guards
      (guard.name :: guard.getChildrenNames).foreach(Environment.addName)
      Right(())
    } else {
      Left(new NameNotUniqueError(s"Error -> Name '${guard.name} is not unique"))
    }
  }

  def removeGuard(guard: Guard): Either[DomainError, _] = {
    if (guards.contains(guard)) {
      guards = guards.filterNot(g => g == guard)
      (guard.name :: guard.getChildrenNames).foreach(Environment.removeName)
      Right(())
    } else {
      Left(new ElementNotFoundError("Guard not found"))
    }
  }

  def getChildrenNames: List[String] = guards.flatMap(g => g.name :: g.getChildrenNames)
}
