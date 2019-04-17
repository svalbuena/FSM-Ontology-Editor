package infrastructure.propertybox.prototypeuri.section

import javafx.scene.control.{Label, TextField}
import javafx.scene.layout.HBox

class PrototypeUriStructureSection extends HBox {
  private val structureLabel = new Label()
  structureLabel.setText("Structure:")

  private val structureTextField = new TextField()

  getChildren.addAll(structureLabel, structureTextField)

  def setStructure(structure: String): Unit = structureTextField.setText(structure)

  def setOnStructureChanged(structureChangedHandler: String => Unit): Unit = {
    structureTextField.setOnKeyTyped(event => {
      structureChangedHandler(structureTextField.getText)
    })
  }
}
