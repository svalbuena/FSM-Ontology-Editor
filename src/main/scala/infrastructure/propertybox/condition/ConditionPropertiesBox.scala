package infrastructure.propertybox.condition

import infrastructure.propertybox.{LabelButtonSection, LabelTextAreaSection, LabelTextFieldSection}
import javafx.scene.layout.VBox

/**
  * Properties box of a condition
  */
class ConditionPropertiesBox extends VBox {
  private val titleAndRemoveSection = new LabelButtonSection
  titleAndRemoveSection.setLabelText("Condition")
  titleAndRemoveSection.setButtonText("Remove")

  private val conditionNameSection = new LabelTextFieldSection
  conditionNameSection.setLabelText("Name:")

  private val conditionQuerySection = new LabelTextAreaSection
  conditionQuerySection.setLabelText("Query:")

  getChildren.addAll(titleAndRemoveSection, conditionNameSection, conditionQuerySection)

  setStyle()


  def setConditionName(name: String): Unit = conditionNameSection.setText(name)

  def setOnConditionNameChanged(conditionNameChangedHandler: String => Unit): Unit = conditionNameSection.setOnTextChanged(conditionNameChangedHandler)

  def setConditionQuery(query: String): Unit = conditionQuerySection.setText(query)

  def setOnConditionQueryChanged(conditionQueryChangedHandler: String => Unit): Unit = conditionQuerySection.setOnTextChanged(conditionQueryChangedHandler)

  def setOnRemoveConditionButtonClicked(callback: () => Unit): Unit = titleAndRemoveSection.setButtonCallback(callback)

  private def setStyle(): Unit = {
    getStyleClass.add("properties-box-vbox")
  }
}
