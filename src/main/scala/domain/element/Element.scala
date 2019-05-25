package domain.element

import domain.environment.Environment
import domain.exception.{DomainError, NameNotUniqueError}


/**
  * Representation of an element of the fsm
  *
  * @param _name name of the element
  */
abstract class Element(private var _name: String, private val environment: Environment) {
  /**
    * Changes the name of an element, error if the name is not unique
    *
    * @param newName new name
    * @return exception or the name
    */
  def name_=(newName: String): Either[Exception, String] = {
    if (!newName.contains(" ")) {
      if (environment.isNameUnique(newName)) {
        environment.removeName(_name)
        environment.addName(newName)
        _name = newName
        Right(name)
      } else Left(new NameNotUniqueError(s"Error -> Name '$name is not unique"))
    } else Left(new DomainError("The name can't contain spaces"))
  }

  def name: String = _name
}