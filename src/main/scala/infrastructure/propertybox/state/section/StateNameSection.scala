package infrastructure.propertybox.state.section

import javafx.scene.control.{Label, TextField}
import javafx.scene.layout.HBox

class StateNameSection extends HBox {
  val nameLabel = new Label
  nameLabel.setText("Name:")

  val nameTextField = new TextField()

  getChildren.addAll(nameLabel, nameTextField)


  def setName(name: String): Unit = {
    nameTextField.setText(name)
  }

  def setOnStateNameChanged(stateNameChangedHandler: String => Unit): Unit = {
    nameTextField.setOnKeyTyped(event => {
      stateNameChangedHandler(nameTextField.getText)
    })
  }
}
