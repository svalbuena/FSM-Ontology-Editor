package infrastructure.element.prototypeuri

import infrastructure.element.Element
import infrastructure.element.prototypeuriparameter.PrototypeUriParameter
import infrastructure.propertybox.prototypeuri.PrototypeUriPropertiesBox

/**
  * Prototype uri data
  * @param name name of the prototype uri
  * @param structure structure of the prototype uri
  */
class PrototypeUri(name: String,
                   var structure: String = ""
                  ) extends Element(name) {

  val propertiesBox = new PrototypeUriPropertiesBox()
  var prototypeParameters: List[PrototypeUriParameter] = List()

  def addPrototypeUriParameter(parameter: PrototypeUriParameter): Unit = {
    prototypeParameters = parameter :: prototypeParameters

    propertiesBox.addParameter(parameter.propertiesBox, parameter.name)
  }

  def removePrototypeUriParameter(parameter: PrototypeUriParameter): Unit = {
    prototypeParameters = prototypeParameters.filterNot(p => p == parameter)

    propertiesBox.removePrototypeUriParameter(parameter.propertiesBox)
  }
}
