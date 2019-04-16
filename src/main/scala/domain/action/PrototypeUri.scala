package domain.action

import domain.{Element, Environment}
import domain.exception.DomainError

class PrototypeUri(name: String,
                   private var _structure: String = "",
                   var prototypeUriParameters: List[PrototypeUriParameter] = List()
                  ) extends Element(name) {


  def this() = this(Environment.generateUniqueName("prototypeUri"))

  def structure: String = _structure
  def structure_= (newStructure: String): Either[DomainError, String] = {
    structure = newStructure
    Right(structure)
  }

  def addPrototypeUriParameter(parameter: PrototypeUriParameter): Either[DomainError, _] = {
    Element.addElementToList(parameter, prototypeUriParameters) match {
      case Left(error) => Left(error)
      case Right(modifiedParameterList) =>
        prototypeUriParameters = modifiedParameterList
        Environment.addName(parameter.name)
        Right()
    }
  }

  def removePrototypeUriParameter(parameter: PrototypeUriParameter): Either[DomainError, _] = {
    Element.removeElementFromList(parameter, prototypeUriParameters) match {
      case Left(error) => Left(error)
      case Right(modifiedParameterList) =>
        prototypeUriParameters = modifiedParameterList
        Environment.addName(parameter.name)
        Right()
    }
  }

  def getChildrenNames: List[String] = prototypeUriParameters.map(_.name)
}
