package infrastructure.propertybox.action.uri.prototype

import infrastructure.elements.action.uri.prototype.parameter.PrototypeParameter
import infrastructure.elements.action.uri.prototype.PrototypeUri
import javafx.scene.control.{Label, ScrollPane, TextField}
import javafx.scene.layout.{HBox, VBox}

class PrototypeUriPropertiesBox extends VBox {
  private val structureSection = new HBox()

  private val structureLabel = new Label()
  structureLabel.setText("Structure:")

  private val structureTextField = new TextField()

  structureSection.getChildren.addAll(structureLabel, structureTextField)

  private val parametersPane = new VBox()

  private val parametersSection = new ScrollPane()
  parametersSection.setContent(parametersPane)

  getChildren.addAll(structureSection, parametersSection)


  def setStructure(structure: String): Unit = structureTextField.setText(structure)

  def addParameter(prototypeParameter: PrototypeParameter): Unit = {
    val prototypeParameterPropertiesBox = prototypeParameter.propertiesBox
    parametersPane.getChildren.add(prototypeParameterPropertiesBox)
  }

  def setOnStructureChanged(structureChangedHandler: String => Unit): Unit = {
    structureTextField.setOnKeyTyped(event => {
      structureChangedHandler(structureTextField.getText)
    })
  }
}
