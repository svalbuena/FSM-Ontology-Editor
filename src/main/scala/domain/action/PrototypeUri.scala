package domain.action

import domain.exception.{DomainError, ElementNotFoundError, NameNotUniqueError}
import domain.{Element, Environment}

class PrototypeUri(name: String,
                   private var _structure: String = "",
                   var prototypeUriParameters: List[PrototypeUriParameter] = List()
                  ) extends Element(name) {


  def this() = this(Environment.generateUniqueName("prototypeUri"))

  def structure: String = _structure

  def structure_=(newStructure: String): Either[DomainError, String] = {
    _structure = newStructure
    Right(structure)
  }

  def addPrototypeUriParameter(parameter: PrototypeUriParameter): Either[DomainError, _] = {
    if (Environment.isNameUnique(parameter.name)) {
      prototypeUriParameters = parameter :: prototypeUriParameters
      Environment.addName(parameter.name)
      Right(())
    } else {
      Left(new NameNotUniqueError(s"Error -> Name '${parameter.name} is not unique"))
    }
  }

  def removePrototypeUriParameter(parameter: PrototypeUriParameter): Either[DomainError, _] = {
    if (prototypeUriParameters.contains(parameter)) {
      prototypeUriParameters = prototypeUriParameters.filterNot(p => p == parameter)
      Environment.removeName(parameter.name)
      Right(())
    } else {
      Left(new ElementNotFoundError("PrototypeUriParameter not found"))
    }
  }

  def getChildrenNames: List[String] = prototypeUriParameters.map(_.name)
}
