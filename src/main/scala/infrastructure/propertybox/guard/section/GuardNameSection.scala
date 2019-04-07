package infrastructure.propertybox.guard.section

import javafx.scene.control.{Button, Label, TextField}
import javafx.scene.layout.HBox

class GuardNameSection extends HBox {
  private val guardNameLabel = new Label()
  guardNameLabel.setText("Name:")

  private val guardNameTextField = new TextField()
  private val removeGuardButton = new Button()
  removeGuardButton.setText("Remove")

  getChildren.addAll(guardNameLabel, guardNameTextField, removeGuardButton)


  def setGuardName(guardName: String): Unit = guardNameTextField.setText(guardName)

  def setOnGuardNameChanged(guardNameChangedHandler: String => Unit): Unit = {
    guardNameTextField.setOnKeyTyped(event => {
      guardNameChangedHandler(guardNameTextField.getText)
    })
  }

  def setOnRemoveGuardButtonClicked(callback: () => Unit): Unit = {
    removeGuardButton.setOnMouseClicked(event => {
      callback()
    })
  }
}
