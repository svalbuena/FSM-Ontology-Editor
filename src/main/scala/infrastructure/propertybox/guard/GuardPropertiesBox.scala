package infrastructure.propertybox.guard

import infrastructure.propertybox.action.ActionPropertiesBox
import infrastructure.propertybox.condition.ConditionPropertiesBox
import infrastructure.propertybox.guard.section.{ActionsSection, ConditionsSection, GuardButtonsSection, GuardNameSection}
import javafx.scene.control.TitledPane
import javafx.scene.layout.VBox

class GuardPropertiesBox extends TitledPane {
  private val guardNameSection = new GuardNameSection()
  private val guardButtonsSection = new GuardButtonsSection()
  private val actionsSection = new ActionsSection()
  private val conditionsSection = new ConditionsSection()

  private val contentPane = new VBox()
  contentPane.getChildren.addAll(guardNameSection, guardButtonsSection, actionsSection, conditionsSection)

  setContent(contentPane)


  def setGuardTitledPaneName(guardName: String): Unit = setText(guardName)

  def setGuardName(guardName: String): Unit = guardNameSection.setGuardName(guardName)

  def setOnGuardNameChanged(guardNameChangedHandler: String => Unit): Unit = guardNameSection.setOnGuardNameChanged(guardNameChangedHandler)

  def setOnRemoveGuardButtonClicked(callback: () => Unit): Unit = guardNameSection.setOnRemoveGuardButtonClicked(callback)

  def setOnAddConditionButtonClicked(callback: () => Unit): Unit = guardButtonsSection.setOnAddConditionButtonClicked(callback)

  def setOnAddActionButtonClicked(callback: () => Unit): Unit = guardButtonsSection.setOnAddActionButtonClicked(callback)

  def addAction(actionPropertiesBox: ActionPropertiesBox): Unit = actionsSection.addAction(actionPropertiesBox)

  def removeAction(actionPropertiesBox: ActionPropertiesBox): Unit = actionsSection.addAction(actionPropertiesBox)

  def addCondition(conditionPropertiesBox: ConditionPropertiesBox): Unit = conditionsSection.addCondition(conditionPropertiesBox)

  def removeCondition(conditionPropertiesBox: ConditionPropertiesBox): Unit = conditionsSection.removeCondition(conditionPropertiesBox)
}
