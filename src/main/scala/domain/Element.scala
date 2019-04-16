package domain

import domain.exception.{ElementNotFoundError, NameNotUniqueError}

abstract class Element(private var _name: String) {
  def name: String = _name

  def name_= (newName: String): Either[NameNotUniqueError, String] = {
    if (Environment.isNameUnique(newName)) {
      name = newName
      Right(name)
    } else {
      Left(new NameNotUniqueError(s"Error -> Name '$name is not unique"))
    }
  }
}

object Element {
  def addElementToList[T <: Element](element: T, elementList: List[T]): Either[NameNotUniqueError, List[T]] = {
    val success = Environment.isNameUnique(element.name)

    if (success) Right(element :: elementList)
    else Left(new NameNotUniqueError(s"Error -> Name '${element.name} is not unique"))
  }

  def removeElementFromList[T <: Element](element: T, elementList: List[T]): Either[ElementNotFoundError, List[T]] = {
    val elementIndex = elementList.indexOf(element)
    val success = elementIndex != -1

    if (success) Right(elementList.filterNot(e => e == element))
    else Left(new ElementNotFoundError)
  }
}
