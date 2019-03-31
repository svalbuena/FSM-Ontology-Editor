package infrastructure.propertybox.action.section

import infrastructure.elements.action.PrototypeParameter
import javafx.scene.control.{Label, ScrollPane, TextField}
import javafx.scene.layout.{HBox, VBox}

class PrototypeUriSection(structure: String, parameters: List[PrototypeParameter]) extends VBox {
  private val structureSection = new HBox()

  private val structureLabel = new Label()
  structureLabel.setText("Structure:")

  private val structureTextField = new TextField()

  structureSection.getChildren.addAll(structureLabel, structureTextField)


  private val parametersSection = new ScrollPane()

  private val parametersPane = new VBox()
  parameters.foreach(parameter => {
    val prototypeParameterSection = new PrototypeParameterSection(parameter.query, parameter.placeholder)
    parametersPane.getChildren.add(prototypeParameterSection)
  })

  parametersSection.setContent(parametersPane)

  getChildren.addAll(structureSection, parametersSection)
}
