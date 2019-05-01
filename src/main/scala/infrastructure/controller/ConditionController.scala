package infrastructure.controller

import application.command.condition.add.AddConditionToGuardCommand
import application.command.condition.modify.{ModifyConditionNameCommand, ModifyConditionQueryCommand}
import application.command.condition.remove.RemoveConditionFromGuardCommand
import application.commandhandler.condition.add.AddConditionToGuardHandler
import application.commandhandler.condition.modify.{ModifyConditionNameHandler, ModifyConditionQueryHandler}
import application.commandhandler.condition.remove.RemoveConditionFromGuardHandler
import infrastructure.element.condition.Condition
import infrastructure.element.guard.Guard

/**
  * Controls the visual and behavior aspects of a condition
  * @param condition condition to control
  */
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

/**
  * Operations that can be done with a condition
  */
object ConditionController {

  /**
    * Creates a condition and adds it to a guard
    * @param guard guard where the condition will be added
    */
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

  /**
    * Modifies the name of a condition
    * @param condition condition to be modified
    * @param newName new name
    */
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

  /**
    * Modifies the query of a condition
    * @param condition condition to be modified
    * @param newQuery new query
    */
  def modifyConditionQuery(condition: Condition, newQuery: String): Unit = {
    new ModifyConditionQueryHandler().execute(new ModifyConditionQueryCommand(condition.name, newQuery)) match {
      case Left(error) => println(error.getMessage)
      case Right(_) =>
        condition.query = newQuery

        println("Condition query changed to -> " + newQuery)
    }
  }

  /**
    * Removes a condition from a guard
    * @param condition condition to be modified
    * @param guard guard where the condition belongs to
    */
  def removeConditionFromGuard(condition: Condition, guard: Guard): Unit = {
    new RemoveConditionFromGuardHandler().execute(new RemoveConditionFromGuardCommand(condition.name, guard.name)) match {
      case Left(error) => println(error.getMessage)
      case Right(_) =>
        guard.removeCondition(condition)

        println("Removing a condition from a guard")
    }
  }

  /**
    * Draws a condition in the application
    * @param condition condition to be drawn
    */
  def drawCondition(condition: Condition): Unit = {
    condition.propertiesBox.setConditionName(condition.name)
    condition.propertiesBox.setConditionQuery(condition.query)

    condition.shape.setConditionName(condition.name)

    condition.parent.parent.shape.updateGuardGroupPosition()

    new ConditionController(condition)
  }
}
