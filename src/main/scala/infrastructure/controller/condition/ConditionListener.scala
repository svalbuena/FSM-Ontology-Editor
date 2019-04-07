package infrastructure.controller.condition

import infrastructure.controller.DrawingPaneController
import infrastructure.elements.condition.Condition

class ConditionListener(condition: Condition, drawingPaneController: DrawingPaneController) {
  private val propertiesBox = condition.propertiesBox
  private val shape = condition.shape

  propertiesBox.setOnConditionNameChanged(conditionName => {
    //TODO: notify the model
    condition.name = conditionName

    shape.setConditionName(conditionName)

    println("Condition name changed to -> " + conditionName)
  })

  propertiesBox.setOnConditionQueryChanged(conditionQuery => {
    //TODO: notify the model
    condition.query = conditionQuery

    println("Condition query changed to -> " + conditionQuery)
  })

  propertiesBox.setOnRemoveConditionButtonClicked(() => {
    //TODO: notify the model
    removeCondition()

    println("Removing a condition from a guard")
  })

  def removeCondition(): Unit = {
    if (condition.hasParent) {
      val guard = condition.getParent

      guard.conditions = guard.conditions.filterNot(c => c == condition)

      drawingPaneController.removeConditionFromGuard(condition, guard)
    }
  }
}
