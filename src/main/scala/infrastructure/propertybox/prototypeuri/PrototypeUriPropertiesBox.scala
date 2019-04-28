package infrastructure.propertybox.prototypeuri

import infrastructure.propertybox.{LabelTextFieldSection, LabelVBoxSection}
import infrastructure.propertybox.prototypeuriparameter.PrototypeUriParameterPropertiesBox
import javafx.scene.control.Button
import javafx.scene.layout.VBox

class PrototypeUriPropertiesBox extends VBox {
  private val structureSection = new LabelTextFieldSection
  structureSection.setLabelText("Structure:")

  private val parametersSection = new LabelVBoxSection[PrototypeUriParameterPropertiesBox]
  parametersSection.setLabelText("Parameters:")
  parametersSection.setButtonText("Add parameter")

  getChildren.addAll(structureSection, parametersSection)


  def setStructure(structure: String): Unit = structureSection.setText(structure)

  def setOnStructureChanged(structureChangedHandler: String => Unit): Unit = structureSection.setOnTextChanged(structureChangedHandler)

  def setParameterPropertiesBoxTitle(propertiesBox: PrototypeUriParameterPropertiesBox, title: String): Unit = parametersSection.setPaneTitle(propertiesBox, title)

  def addParameter(propertiesBox: PrototypeUriParameterPropertiesBox, title: String): Unit = parametersSection.addPane(propertiesBox, title)

  def removePrototypeUriParameter(propertiesBox: PrototypeUriParameterPropertiesBox): Unit = parametersSection.removePane(propertiesBox)

  def setOnAddParameterButtonClicked(callback: () => Unit): Unit = parametersSection.setButtonCallback(callback)
}
