package infrastructure.propertybox.condition

import infrastructure.propertybox.{LabelTextAreaSection, LabelTextFieldSection}
import javafx.scene.control.Button
import javafx.scene.layout.VBox

class ConditionPropertiesBox extends VBox {
  private val removeConditionButton = new Button()
  removeConditionButton.setText("Remove")

  private val conditionNameSection = new LabelTextFieldSection
  conditionNameSection.setLabelText("Name:")

  private val conditionQuerySection = new LabelTextAreaSection
  conditionQuerySection.setLabelText("Query:")

  getChildren.addAll(removeConditionButton, conditionNameSection, conditionQuerySection)


  def setConditionName(name: String): Unit = conditionNameSection.setText(name)

  def setOnConditionNameChanged(conditionNameChangedHandler: String => Unit): Unit = conditionNameSection.setOnTextChanged(conditionNameChangedHandler)

  def setConditionQuery(query: String): Unit = conditionQuerySection.setText(query)

  def setOnConditionQueryChanged(conditionQueryChangedHandler: String => Unit): Unit = conditionQuerySection.setOnTextChanged(conditionQueryChangedHandler)

  def setOnRemoveConditionButtonClicked(callback: () => Unit): Unit = removeConditionButton.setOnAction(_ => callback())
}
