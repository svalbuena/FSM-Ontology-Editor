package domain.action

import domain.exception.{DomainError, ElementNotFoundError, NameNotUniqueError}
import domain.{Element, Environment}

/**
  *
  * @param name                   name of the prototype uri
  * @param _structure             structure of the prototype uri
  * @param prototypeUriParameters paremeters of the prototype uri
  */
class PrototypeUri(name: String,
                   private var _structure: String = "",
                   var prototypeUriParameters: List[PrototypeUriParameter] = List(),
                   environment: Environment
                  ) extends Element(name, environment) {


  def this(environment: Environment) = this(environment.generateUniqueName("prototypeUri"), environment = environment)

  /**
    *
    * @param newStructure new structure of the body
    * @return exception or the structure
    */
  def structure_=(newStructure: String): Either[DomainError, String] = {
    _structure = newStructure
    Right(structure)
  }

  def structure: String = _structure

  /**
    * Adds a parameter to the prototype uri, error if the name of the parameter is not unique
    *
    * @param parameter parameter to be added
    * @return exception or nothing if successful
    */
  def addPrototypeUriParameter(parameter: PrototypeUriParameter): Either[DomainError, _] = {
    if (environment.isNameUnique(parameter.name)) {
      prototypeUriParameters = parameter :: prototypeUriParameters
      environment.addName(parameter.name)
      Right(())
    } else {
      Left(new NameNotUniqueError(s"Error -> Name '${parameter.name} is not unique"))
    }
  }

  /**
    * Removes a parameter from the prototype uri, error if the parameter doesn't belong to this prototype uri
    *
    * @param parameter parameter to remove
    * @return exception or nothing if successful
    */
  def removePrototypeUriParameter(parameter: PrototypeUriParameter): Either[DomainError, _] = {
    if (prototypeUriParameters.contains(parameter)) {
      prototypeUriParameters = prototypeUriParameters.filterNot(p => p == parameter)
      environment.removeName(parameter.name)
      Right(())
    } else {
      Left(new ElementNotFoundError("PrototypeUriParameter not found"))
    }
  }

  /**
    *
    * @return the names of the parameters
    */
  def getChildrenNames: List[String] = prototypeUriParameters.map(_.name)
}
