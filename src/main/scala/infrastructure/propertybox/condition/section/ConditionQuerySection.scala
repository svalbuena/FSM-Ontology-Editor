package infrastructure.propertybox.condition.section

import javafx.scene.control.{Label, TextField}
import javafx.scene.layout.VBox

class ConditionQuerySection extends VBox {
  private val conditionQueryLabel = new Label()
  conditionQueryLabel.setText("Query:")

  private val conditionQueryTextField = new TextField()

  getChildren.addAll(conditionQueryLabel, conditionQueryTextField)


  def setConditionQuery(query: String): Unit = conditionQueryTextField.setText(query)

  def setOnConditionQueryChanged(conditionQueryChangedHandler: String => Unit): Unit = {
    conditionQueryTextField.setOnKeyTyped(event => {
      conditionQueryChangedHandler(conditionQueryTextField.getText)
    })
  }
}
