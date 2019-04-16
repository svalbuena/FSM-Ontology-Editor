package domain.transition

import domain.{Element, Environment}
import domain.exception.DomainError
import domain.guard.Guard
import domain.state.State

class Transition(name: String,
                 val source: State,
                 val destination: State,
                 var guards: List[Guard] = List()
                ) extends Element(name) {


  def this(source: State, destination: State) = this(Environment.generateUniqueName("transition"), source, destination)

  def addGuard(guard: Guard): Either[DomainError, _] = {
    Element.addElementToList(guard, guards) match {
      case Left(error) => Left(error)
      case Right(modifiedGuardList) =>
        guards = modifiedGuardList
        (guard.name :: guard.getChildrenNames).foreach(Environment.addName)
        Right()
    }
  }

  def removeGuard(guard: Guard): Either[DomainError, _] = {
    Element.removeElementFromList(guard, guards) match {
      case Left(error) => Left(error)
      case Right(modifiedGuardList) =>
        guards = modifiedGuardList
        (guard.name :: guard.getChildrenNames).foreach(Environment.removeName)
        Right()
    }
  }

  def getChildrenNames: List[String] = guards.flatMap(g => g.name :: g.getChildrenNames)
}
