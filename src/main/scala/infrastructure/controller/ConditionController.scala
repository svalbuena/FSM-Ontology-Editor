package infrastructure.controller

import application.command.condition.add.AddConditionToGuardCommand
import application.command.condition.modify.{ModifyConditionNameCommand, ModifyConditionQueryCommand}
import application.command.condition.remove.RemoveConditionFromGuardCommand
import application.commandhandler.condition.add.AddConditionToGuardHandler
import application.commandhandler.condition.modify.{ModifyConditionNameHandler, ModifyConditionQueryHandler}
import application.commandhandler.condition.remove.RemoveConditionFromGuardHandler
import infrastructure.element.condition.Condition
import infrastructure.element.guard.Guard
import infrastructure.element.transition.Transition

class ConditionController(condition: Condition) {
  private val propertiesBox = condition.propertiesBox
  private val shape = condition.shape

  propertiesBox.setOnConditionNameChanged(newName => ConditionController.modifyConditionName(condition, newName))

  propertiesBox.setOnConditionQueryChanged(newQuery => ConditionController.modifyConditionQuery(condition, newQuery))

  propertiesBox.setOnRemoveConditionButtonClicked(() => removeCondition())

  private def removeCondition(): Unit = {
    val guard = condition.parent
    ConditionController.removeConditionFromGuard(condition, guard)
  }
}

object ConditionController {
  def addConditionToGuard(guard: Guard): Unit = {
    new AddConditionToGuardHandler().execute(new AddConditionToGuardCommand(guard.name)) match {
      case Left(error) => println(error.getMessage)
      case Right(conditionName) =>
        val condition = new Condition(conditionName, parent = guard)

        guard.addCondition(condition)

        drawCondition(condition)

        println("Adding a guard ")
    }
  }

  def modifyConditionName(condition: Condition, newName: String): Unit = {
    new ModifyConditionNameHandler().execute(new ModifyConditionNameCommand(condition.name, newName)) match {
      case Left(error) => println(error.getMessage)
      case Right(_) =>
        condition.name = newName

        condition.shape.setConditionName(newName)

        condition.parent.propertiesBox.setConditionPropertiesBoxTitle(condition.propertiesBox, condition.name)

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

  def removeConditionFromGuard(condition: Condition, guard: Guard): Unit = {
    new RemoveConditionFromGuardHandler().execute(new RemoveConditionFromGuardCommand(condition.name, guard.name)) match {
      case Left(error) => println(error.getMessage)
      case Right(_) =>
        guard.removeCondition(condition)

        println("Removing a condition from a guard")
    }
  }

  def drawCondition(condition: Condition): Unit = {
    condition.propertiesBox.setConditionName(condition.name)
    condition.propertiesBox.setConditionQuery(condition.query)

    condition.shape.setConditionName(condition.name)

    condition.parent.parent.shape.updateGuardGroupPosition()

    new ConditionController(condition)
  }
}
