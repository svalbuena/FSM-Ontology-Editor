package infrastructure.controller.condition

import application.command.condition.add.AddConditionToGuardCommand
import application.command.condition.modify.{ModifyConditionNameCommand, ModifyConditionQueryCommand}
import application.command.condition.remove.RemoveConditionFromGuardCommand
import application.commandhandler.condition.add.AddConditionToGuardHandler
import application.commandhandler.condition.modify.{ModifyConditionNameHandler, ModifyConditionQueryHandler}
import application.commandhandler.condition.remove.RemoveConditionFromGuardHandler
import infrastructure.controller.DrawingPaneController
import infrastructure.element.condition.Condition
import infrastructure.element.guard.Guard

class ConditionController(condition: Condition, drawingPaneController: DrawingPaneController) {
  private val propertiesBox = condition.propertiesBox
  private val shape = condition.shape

  propertiesBox.setOnConditionNameChanged(newName => ConditionController.modifyConditionName(condition, newName))

  propertiesBox.setOnConditionQueryChanged(newQuery => ConditionController.modifyConditionQuery(condition, newQuery))

  propertiesBox.setOnRemoveConditionButtonClicked(() => removeCondition())

  private def removeCondition(): Unit = {
    if (condition.hasParent) {
      val guard = condition.getParent
      ConditionController.removeConditionFromGuard(condition, guard, drawingPaneController)
    }
  }
}

object ConditionController {
  def addConditionToGuard(guard: Guard, drawingPaneController: DrawingPaneController): Unit = {
    new AddConditionToGuardHandler().execute(new AddConditionToGuardCommand(guard.name)) match {
      case Left(error) => println(error.getMessage)
      case Right(conditionName) =>
        val condition = new Condition(conditionName)

        guard.conditions = condition :: guard.conditions

        drawingPaneController.addConditionToGuard(condition, guard)

        println("Adding a guard ")
    }
  }

  def modifyConditionName(condition: Condition, newName: String): Unit = {
    new ModifyConditionNameHandler().execute(new ModifyConditionNameCommand(condition.name, newName)) match {
      case Left(error) => println(error.getMessage)
      case Right(_) =>
        condition.name = newName

        condition.shape.setConditionName(newName)

        println("Condition name changed to -> " + newName)
    }
  }

  def modifyConditionQuery(condition: Condition, newQuery: String): Unit = {
    new ModifyConditionQueryHandler().execute(new ModifyConditionQueryCommand(condition.name, newQuery)) match {
      case Left(error) => println(error.getMessage)
      case Right(_) =>
        condition.query = newQuery

        println("Condition query changed to -> " + newQuery)
    }
  }

  def removeConditionFromGuard(condition: Condition, guard: Guard, drawingPaneController: DrawingPaneController): Unit = {
    new RemoveConditionFromGuardHandler().execute(new RemoveConditionFromGuardCommand(condition.name, guard.name)) match {
      case Left(error) => println(error.getMessage)
      case Right(_) =>
        guard.conditions = guard.conditions.filterNot(c => c == condition)

        drawingPaneController.removeConditionFromGuard(condition, guard)

        println("Removing a condition from a guard")
    }
  }
}
