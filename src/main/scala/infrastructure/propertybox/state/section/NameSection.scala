package infrastructure.propertybox.state.section

import javafx.event.EventHandler
import javafx.scene.Cursor
import javafx.scene.control.{Label, TextField}
import javafx.scene.input.KeyEvent
import javafx.scene.layout.HBox

class NameSection(name: String) extends HBox {
  val nameLabel = new Label
  nameLabel.setText("Name:")

  val nameTextField = new TextField()
  nameTextField.setText(name)

  getChildren.addAll(nameLabel, nameTextField)

  def getName: String = nameTextField.getText

  def setName(name: String): Unit = {
    nameTextField.setText(name)
  }

  def setOnNameEditedListener(eventHandler: EventHandler[KeyEvent]): Unit = {
    nameTextField.setOnKeyTyped(eventHandler)
  }
}
