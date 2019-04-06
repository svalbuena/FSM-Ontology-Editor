package infrastructure.propertybox.action.uri.prototype

import infrastructure.elements.action.uri.prototype.parameter.PrototypeParameter
import infrastructure.elements.action.uri.prototype.PrototypeUri
import infrastructure.propertybox.action.uri.prototype.parameter.{PrototypeUriParameterPropertiesBox, PrototypeUriParametersSection}
import infrastructure.propertybox.action.uri.prototype.structure.PrototypeUriStructureSection
import javafx.scene.control.{Label, ScrollPane, TextField}
import javafx.scene.layout.{HBox, VBox}

class PrototypeUriPropertiesBox extends VBox {
  private val structureSection = new PrototypeUriStructureSection()
  private val parametersSection = new PrototypeUriParametersSection()

  getChildren.addAll(structureSection, parametersSection)

  def setStructure(structure: String): Unit = structureSection.setStructure(structure)
  def setOnStructureChanged(structureChangedHandler: String => Unit): Unit = structureSection.setOnStructureChanged(structureChangedHandler)
  def addParameter(prototypeUriParameterPropertiesBox: PrototypeUriParameterPropertiesBox): Unit = parametersSection.addPrototypeUriParameter(prototypeUriParameterPropertiesBox)
  def removePrototypeUriParameter(prototypeUriParameterPropertiesBox: PrototypeUriParameterPropertiesBox): Unit = parametersSection.removePrototypeUriParameter(prototypeUriParameterPropertiesBox)
  def setOnAddParameterButtonClicked(callback: () => Unit): Unit = parametersSection.setOnAddParameterButtonClicked(callback)
}
