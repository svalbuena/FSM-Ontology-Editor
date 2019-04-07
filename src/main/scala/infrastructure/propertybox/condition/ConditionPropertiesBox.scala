package infrastructure.propertybox.condition

import infrastructure.propertybox.condition.section.{ConditionNameSection, ConditionQuerySection}
import javafx.scene.layout.VBox

class ConditionPropertiesBox extends VBox {
  private val conditionNameSection = new ConditionNameSection()
  private val conditionQuerySection = new ConditionQuerySection()

  getChildren.addAll(conditionNameSection, conditionQuerySection)


  def setConditionName(name: String): Unit = conditionNameSection.setConditionName(name)

  def setOnConditionNameChanged(conditionNameChangedHandler: String => Unit): Unit = conditionNameSection.setOnConditionNameChanged(conditionNameChangedHandler)

  def setConditionQuery(query: String): Unit = conditionQuerySection.setConditionQuery(query)

  def setOnConditionQueryChanged(conditionQueryChangedHandler: String => Unit): Unit = conditionQuerySection.setOnConditionQueryChanged(conditionQueryChangedHandler)

  def setOnRemoveConditionButtonClicked(callback: () => Unit): Unit = conditionNameSection.setOnRemoveConditionButtonClicked(callback)
}
