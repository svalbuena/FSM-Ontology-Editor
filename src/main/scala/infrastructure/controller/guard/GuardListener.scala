package infrastructure.controller.guard

import infrastructure.controller.DrawingPaneController
import infrastructure.elements.action.{Action, ActionType}
import infrastructure.elements.condition.Condition
import infrastructure.elements.guard.Guard
import infrastructure.id.IdGenerator

class GuardListener(guard: Guard, drawingPaneController: DrawingPaneController, idGenerator: IdGenerator) {
  private val propertiesBox = guard.propertiesBox
  private val shape = guard.shape

  propertiesBox.setOnGuardNameChanged(guardName => {
    //TODO: notify the model
    guard.name = guardName
    println("Guard name changed to -> " + guardName)

    shape.setGuardName(guardName)
  })

  propertiesBox.setOnAddActionButtonClicked(() => {
    //TODO: notify the model
    addAction()
    println("Adding an action to a guard")
  })

  propertiesBox.setOnAddConditionButtonClicked(() => {
    //TODO: notify the model
    addCondition()
    println("Adding a condition to a guard")
  })

  propertiesBox.setOnRemoveGuardButtonClicked(() => {
    //TODO: notify the model
    removeGuard()
    println("Removing a guard")
  })

  private def addAction(): Unit = {
    val id = idGenerator.getId
    val action = new Action(id, ActionType.GUARD, "Action" + id)

    guard.actions = action :: guard.actions

    drawingPaneController.addActionToGuard(action, guard)
  }

  private def addCondition(): Unit = {
    val id = idGenerator.getId
    val condition = new Condition(id, "Condition" + id, "")

    guard.conditions = condition :: guard.conditions

    drawingPaneController.addConditionToGuard(condition, guard)
  }

  private def removeGuard(): Unit = {
    if (guard.hasParent) {
      val transition = guard.getParent

      transition.guards = transition.guards.filterNot(g => g == guard)

      drawingPaneController.removeGuardFromTransition(guard, transition)
    }
  }
}
