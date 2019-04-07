package infrastructure.controller.condition

import infrastructure.controller.InfrastructureController
import infrastructure.elements.condition.Condition

class ConditionListener(condition: Condition, infrastructureController: InfrastructureController) {
  private val propertiesBox = condition.propertiesBox
  private val shape = condition.shape

  propertiesBox.setOnConditionNameChanged(conditionName => {
    //TODO: notify the model
    condition.name = conditionName

    shape.setConditionName(conditionName)
  })

  propertiesBox.setOnConditionQueryChanged(conditionQuery => {
    //TODO: notify the model
    condition.query = conditionQuery
  })

  propertiesBox.setOnRemoveConditionButtonClicked(() => {
    removeCondition()
  })

  def removeCondition(): Unit = {
    if (condition.hasParent) {
      val guard = condition.getParent

      guard.conditions = guard.conditions.filterNot(c => c == condition)

      infrastructureController.removeConditionFromGuard(condition, guard)
    }
  }
}
