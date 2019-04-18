package infrastructure.element.prototypeuri

import infrastructure.element.Element
import infrastructure.element.prototypeuriparameter.PrototypeUriParameter
import infrastructure.propertybox.prototypeuri.PrototypeUriPropertiesBox

class PrototypeUri(name: String,
                   var structure: String = ""
                  ) extends Element(name) {

  val propertiesBox = new PrototypeUriPropertiesBox()
  var prototypeParameters: List[PrototypeUriParameter] = List()

  def addPrototypeUriParameter(parameter: PrototypeUriParameter): Unit = {
    prototypeParameters = parameter :: prototypeParameters

    propertiesBox.addParameter(parameter.propertiesBox)
  }

  def removePrototypeUriParameter(parameter: PrototypeUriParameter): Unit = {
    prototypeParameters = prototypeParameters.filterNot(p => p == parameter)

    propertiesBox.removePrototypeUriParameter(parameter.propertiesBox)
  }

}