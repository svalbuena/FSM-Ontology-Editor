package infrastructure.propertybox.prototypeuri

import infrastructure.propertybox.prototypeuriparameter.PrototypeUriParameterPropertiesBox
import infrastructure.propertybox.{LabelTextFieldSection, LabelVBoxSection}
import javafx.scene.layout.VBox

class PrototypeUriPropertiesBox extends VBox {
  private val nameSection = new LabelTextFieldSection
  nameSection.setLabelText("Name:")

  private val structureSection = new LabelTextFieldSection
  structureSection.setLabelText("Structure:")

  private val parametersSection = new LabelVBoxSection[PrototypeUriParameterPropertiesBox]("parameter-content-titled-pane")
  parametersSection.setLabelText("Parameters")
  parametersSection.setButtonText("Add parameter")

  getChildren.addAll(nameSection, structureSection, parametersSection)

  setStyle()

  def setName(name: String): Unit = nameSection.setText(name)

  def setOnNameChanged(nameChangedHandler: String => Unit): Unit = structureSection.setOnTextChanged(nameChangedHandler)

  def setStructure(structure: String): Unit = structureSection.setText(structure)

  def setOnStructureChanged(structureChangedHandler: String => Unit): Unit = structureSection.setOnTextChanged(structureChangedHandler)

  def setParameterPropertiesBoxTitle(propertiesBox: PrototypeUriParameterPropertiesBox, title: String): Unit = parametersSection.setPaneTitle(propertiesBox, title)

  def addParameter(propertiesBox: PrototypeUriParameterPropertiesBox, title: String): Unit = parametersSection.addPane(propertiesBox, title)

  def removePrototypeUriParameter(propertiesBox: PrototypeUriParameterPropertiesBox): Unit = parametersSection.removePane(propertiesBox)

  def setOnAddParameterButtonClicked(callback: () => Unit): Unit = parametersSection.setButtonCallback(callback)

  private def setStyle(): Unit = {
    getStyleClass.add("properties-box-vbox")
  }
}
