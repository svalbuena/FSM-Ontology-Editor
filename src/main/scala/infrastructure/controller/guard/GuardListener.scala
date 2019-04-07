package infrastructure.controller.guard

import infrastructure.controller.InfrastructureController
import infrastructure.elements.action.{Action, ActionType}
import infrastructure.elements.condition.Condition
import infrastructure.elements.guard.Guard
import infrastructure.id.IdGenerator

class GuardListener(guard: Guard, infrastructureController: InfrastructureController, idGenerator: IdGenerator) {
  private val propertiesBox = guard.propertiesBox
  private val shape = guard.shape

  propertiesBox.setOnGuardNameChanged(guardName => {
    //TODO: notify the model
    guard.name = guardName

    shape.setGuardName(guardName)
  })

  propertiesBox.setOnAddActionButtonClicked(() => {
    addAction()
  })

  propertiesBox.setOnAddConditionButtonClicked(() => {
    addCondition()
  })

  propertiesBox.setOnRemoveGuardButtonClicked(() => {
    removeGuard()
  })

  private def addAction(): Unit = {
    val id = idGenerator.getId
    val action = new Action(id, ActionType.GUARD, "Action" + id)

    guard.actions = action :: guard.actions

    infrastructureController.addActionToGuard(action, guard)
  }

  private def addCondition(): Unit = {
    val id = idGenerator.getId
    val condition = new Condition(id, "Condition" + id, "")

    guard.conditions = condition :: guard.conditions

    infrastructureController.addConditionToGuard(condition, guard)
  }

  private def removeGuard(): Unit = {
    if (guard.hasParent) {
      val transition = guard.getParent

      transition.guards = transition.guards.filterNot(g => g == guard)

      infrastructureController.removeGuardFromTransition(guard, transition)
    }
  }
}
