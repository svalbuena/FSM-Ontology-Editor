package infrastructure.propertybox.action.section.uri.prototype

import infrastructure.propertybox.action.section.uri.prototype.parameter.{PrototypeUriParameterPropertiesBox, PrototypeUriParametersSection}
import infrastructure.propertybox.action.section.uri.prototype.structure.PrototypeUriStructureSection
import javafx.scene.layout.VBox

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
