package infrastructure.propertybox.state.section

import javafx.event.EventHandler
import javafx.scene.Cursor
import javafx.scene.control.{Label, TextField}
import javafx.scene.input.KeyEvent
import javafx.scene.layout.HBox

class NameSection extends HBox {
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
