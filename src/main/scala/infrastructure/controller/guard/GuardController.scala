package infrastructure.controller.guard

import application.command.guard.add.AddGuardToTransitionCommand
import application.command.guard.modify.ModifyGuardNameCommand
import application.command.guard.remove.RemoveGuardFromTransitionCommand
import application.commandhandler.guard.add.AddGuardToTransitionHandler
import application.commandhandler.guard.modify.ModifyGuardNameHandler
import application.commandhandler.guard.remove.RemoveGuardFromTransitionHandler
import infrastructure.controller.DrawingPaneController
import infrastructure.controller.action.ActionController
import infrastructure.controller.condition.ConditionController
import infrastructure.element.guard.Guard
import infrastructure.element.transition.Transition
import infrastructure.id.IdGenerator

class GuardController(guard: Guard, drawingPaneController: DrawingPaneController, idGenerator: IdGenerator) {
  private val propertiesBox = guard.propertiesBox
  private val shape = guard.shape

  propertiesBox.setOnGuardNameChanged(newName => GuardController.modifyGuardName(guard, newName))

  propertiesBox.setOnAddActionButtonClicked(() => addActionToGuard())

  propertiesBox.setOnAddConditionButtonClicked(() => addConditionToGuard())

  propertiesBox.setOnRemoveGuardButtonClicked(() => removeGuardFromTransition())

  private def addActionToGuard(): Unit = ActionController.addActionToGuard(guard, drawingPaneController)

  private def addConditionToGuard(): Unit = ConditionController.addConditionToGuard(guard, drawingPaneController)

  private def removeGuardFromTransition(): Unit = {
    if (guard.hasParent) {
      val transition = guard.getParent
      GuardController.removeGuardFromTransition(guard, transition, drawingPaneController)
    }
  }
}

object GuardController {
  def addGuardToTransition(transition: Transition, drawingPaneController: DrawingPaneController): Unit = {
    new AddGuardToTransitionHandler().execute(new AddGuardToTransitionCommand(transition.name)) match {
      case Left(error) => println(error.getMessage)
      case Right(guardName) =>
        val guard = new Guard(guardName)

        transition.guards = guard :: transition.guards

        drawingPaneController.addGuardToTransition(guard, transition)

        println("Adding a guard to a transition")
    }
  }

  def modifyGuardName(guard: Guard, newName: String): Unit = {
    new ModifyGuardNameHandler().execute(new ModifyGuardNameCommand(guard.name, newName)) match {
      case Left(error) => println(error.getMessage)
      case Right(_) =>
        guard.name = newName
        println("Guard name changed to -> " + newName)

        guard.shape.setGuardName(newName)
    }
  }

  def removeGuardFromTransition(guard: Guard, transition: Transition, drawingPaneController: DrawingPaneController): Unit = {
    new RemoveGuardFromTransitionHandler().execute(new RemoveGuardFromTransitionCommand(guard.name, transition.name)) match {
      case Left(error) => println(error.getMessage)
      case Right(_) =>
        transition.guards = transition.guards.filterNot(g => g == guard)

        drawingPaneController.removeGuardFromTransition(guard, transition)

        println("Removing a guard from a transition")
    }
  }
}
