package infrastructure.propertybox.fsm.section

import javafx.scene.control.{Label, TextField}
import javafx.scene.layout.HBox

class FsmNameSection extends HBox {
  private val nameLabel = new Label()
  nameLabel.setText("Name:")

  private val nameTextField = new TextField()


  getChildren.addAll(nameLabel, nameTextField)


  def setFsmName(fsmName: String): Unit = nameTextField.setText(fsmName)

  def setOnFsmNameChanged(fsmNameChangedHandler: String => Unit): Unit = {
    nameTextField.setOnKeyTyped(_ => {
      fsmNameChangedHandler(nameTextField.getText)
    })
  }
}
