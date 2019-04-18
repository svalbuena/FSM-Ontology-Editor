package infrastructure.propertybox.body.section

import javafx.scene.control.{Label, TextField}
import javafx.scene.layout.HBox

class BodyNameSection extends HBox {
  private val nameLabel = new Label()
  nameLabel.setText("Name:")

  private val nameTextField = new TextField()

  getChildren.addAll(nameLabel, nameTextField)

  def setBodyName(name: String): Unit = nameTextField.setText(name)

  def setOnBodyNameChanged(bodyNameChangedHandler: String => Unit): Unit = {
    nameTextField.setOnKeyTyped(event => {
      bodyNameChangedHandler(nameTextField.getText)
    })
  }
}
