package domain

import domain.exception.NameNotUniqueError

abstract class Element(private var _name: String) {
  def name: String = _name

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