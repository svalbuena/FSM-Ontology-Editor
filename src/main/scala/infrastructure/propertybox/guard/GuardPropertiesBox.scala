package infrastructure.propertybox.guard

import infrastructure.propertybox.action.ActionPropertiesBox
import infrastructure.propertybox.condition.ConditionPropertiesBox
import infrastructure.propertybox.{LabelTextFieldSection, LabelVBoxSection}
import javafx.scene.control.Button
import javafx.scene.layout.VBox

/**
  * Properties box of a guard
  */
class GuardPropertiesBox extends VBox {
  private val removeGuardButton = new Button()
  removeGuardButton.setText("Remove")

  private val guardNameSection = new LabelTextFieldSection
  guardNameSection.setLabelText("Name:")

  private val actionsSection = new LabelVBoxSection[ActionPropertiesBox]("guard-action-titled-pane")
  actionsSection.setLabelText("Actions:")
  actionsSection.setButtonText("Add action")

  private val conditionsSection = new LabelVBoxSection[ConditionPropertiesBox]("guard-condition-titled-pane")
  conditionsSection.setLabelText("Conditions:")
  conditionsSection.setButtonText("Add condition")

  getChildren.addAll(guardNameSection, actionsSection, conditionsSection)

  setStyle()


  def setGuardName(guardName: String): Unit = guardNameSection.setText(guardName)

  def setOnGuardNameChanged(guardNameChangedHandler: String => Unit): Unit = guardNameSection.setOnTextChanged(guardNameChangedHandler)

  def setOnAddActionButtonClicked(callback: () => Unit): Unit = actionsSection.setButtonCallback(callback)

  def setOnRemoveGuardButtonClicked(callback: () => Unit): Unit = removeGuardButton.setOnAction(_ => callback())

  def setOnAddConditionButtonClicked(callback: () => Unit): Unit = conditionsSection.setButtonCallback(callback)

  def addAction(actionPropertiesBox: ActionPropertiesBox, title: String): Unit = actionsSection.addPane(actionPropertiesBox, title)

  def setActionPropertiesBoxTitle(actionPropertiesBox: ActionPropertiesBox, title: String): Unit = actionsSection.setPaneTitle(actionPropertiesBox, title)

  def removeAction(actionPropertiesBox: ActionPropertiesBox): Unit = actionsSection.removePane(actionPropertiesBox)

  def addCondition(conditionPropertiesBox: ConditionPropertiesBox, title: String): Unit = conditionsSection.addPane(conditionPropertiesBox, title)

  def setConditionPropertiesBoxTitle(conditionPropertiesBox: ConditionPropertiesBox, title: String): Unit = conditionsSection.setPaneTitle(conditionPropertiesBox, title)

  def removeCondition(conditionPropertiesBox: ConditionPropertiesBox): Unit = conditionsSection.removePane(conditionPropertiesBox)

  private def setStyle(): Unit = {
    getStyleClass.add("properties-box-vbox")
  }
}
