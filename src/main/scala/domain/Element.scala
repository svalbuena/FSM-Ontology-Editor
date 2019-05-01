package domain

import domain.exception.NameNotUniqueError

/**
  * Representation of an element of the fsm
  *
  * @param _name name of the element
  */
abstract class Element(private var _name: String) {
  def name: String = _name

  /**
    * Changes the name of an element, error if the name is not unique
    *
    * @param newName new name
    * @return exception or the name
    */
  def name_=(newName: String): Either[NameNotUniqueError, String] = {
    if (Environment.isNameUnique(newName)) {
      Environment.removeName(_name)
      Environment.addName(newName)
      _name = newName
      Right(name)
    } else {
      Left(new NameNotUniqueError(s"Error -> Name '$name is not unique"))
    }
  }
}