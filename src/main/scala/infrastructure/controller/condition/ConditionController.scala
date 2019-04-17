package infrastructure.controller.condition

import infrastructure.controller.DrawingPaneController
import infrastructure.elements.condition.Condition

class ConditionController(condition: Condition, drawingPaneController: DrawingPaneController) {
  private val propertiesBox = condition.propertiesBox
  private val shape = condition.shape

  propertiesBox.setOnConditionNameChanged(conditionName => {
    //TODO: notify the model, ModifyConditionName
    condition.name = conditionName

    shape.setConditionName(conditionName)

    println("Condition name changed to -> " + conditionName)
  })

  propertiesBox.setOnConditionQueryChanged(conditionQuery => {
    //TODO: notify the model, ModifyConditionQuery
    condition.query = conditionQuery

    println("Condition query changed to -> " + conditionQuery)
  })

  propertiesBox.setOnRemoveConditionButtonClicked(() => {
    removeCondition()
  })

  def removeCondition(): Unit = {
    println("Removing a condition from a guard")
    if (condition.hasParent) {
      //TODO: notify the model, RemoveConditionFromGuard

      val guard = condition.getParent

      guard.conditions = guard.conditions.filterNot(c => c == condition)

      drawingPaneController.removeConditionFromGuard(condition, guard)
    }
  }
}

object ConditionController {
  def addConditionToGuard(): Unit = {

  }

  def modifyConditionName(): Unit = {

  }

  def modifyConditionQuery(): Unit = {

  }

  def removeConditionFromGuard(): Unit = {

  }
}
