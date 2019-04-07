package infrastructure.propertybox.condition.section

import javafx.scene.control.{Button, Label, TextField}
import javafx.scene.layout.HBox

class ConditionNameSection extends HBox {
  private val conditionNameLabel = new Label()
  conditionNameLabel.setText("Name:")

  private val conditionNameTextField = new TextField()

  private val removeConditionButton = new Button()
  removeConditionButton.setText("Remove")

  getChildren.addAll(conditionNameLabel, conditionNameTextField)


  def setConditionName(name: String): Unit = conditionNameTextField.setText(name)

  def setOnConditionNameChanged(conditionNameChangedHandler: String => Unit): Unit = {
    conditionNameTextField.setOnKeyTyped(event => {
      conditionNameChangedHandler(conditionNameTextField.getText)
    })
  }

  def setOnRemoveConditionButtonClicked(callback: () => Unit): Unit = {
    removeConditionButton.setOnMouseClicked(event => {
      callback()
    })
  }
}
