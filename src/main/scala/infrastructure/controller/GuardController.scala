package infrastructure.controller

import application.command.guard.add.AddGuardToTransitionCommand
import application.command.guard.modify.ModifyGuardNameCommand
import application.command.guard.remove.RemoveGuardFromTransitionCommand
import application.commandhandler.guard.add.AddGuardToTransitionHandler
import application.commandhandler.guard.modify.ModifyGuardNameHandler
import application.commandhandler.guard.remove.RemoveGuardFromTransitionHandler
import infrastructure.element.guard.Guard
import infrastructure.element.transition.Transition

/**
  * Controls the visual and behavior aspects of a guard
  * @param guard guard to control
  */
class GuardController(guard: Guard) {
  private val propertiesBox = guard.propertiesBox
  private val shape = guard.shape

  propertiesBox.setOnGuardNameChanged(newName => GuardController.modifyGuardName(guard, newName))

  propertiesBox.setOnAddActionButtonClicked(() => addActionToGuard())

  propertiesBox.setOnAddConditionButtonClicked(() => addConditionToGuard())

  propertiesBox.setOnRemoveGuardButtonClicked(() => removeGuardFromTransition())

  private def addActionToGuard(): Unit = ActionController.addActionToGuard(guard)

  private def addConditionToGuard(): Unit = ConditionController.addConditionToGuard(guard)

  private def removeGuardFromTransition(): Unit = {
    val transition = guard.parent
    GuardController.removeGuardFromTransition(guard, transition)
  }
}

/**
  * Operations that can be done with a Guard
  */
object GuardController {

  /**
    * Creates a guard and adds it to a transition
    * @param transition transiton where the guard will be added
    */
  def addGuardToTransition(transition: Transition): Unit = {
    new AddGuardToTransitionHandler().execute(new AddGuardToTransitionCommand(transition.name)) match {
      case Left(error) => println(error.getMessage)
      case Right(guardName) =>
        val guard = new Guard(guardName, parent = transition)

        transition.addGuard(guard)

        drawGuard(guard)

        println("Adding a guard to a transition")
    }
  }

  /**
    * Modifies the name of a guard
    * @param guard guard to be modified
    * @param newName new name
    */
  def modifyGuardName(guard: Guard, newName: String): Unit = {
    new ModifyGuardNameHandler().execute(new ModifyGuardNameCommand(guard.name, newName)) match {
      case Left(error) => println(error.getMessage)
      case Right(_) =>
        guard.name = newName
        println("Guard name changed to -> " + newName)

        guard.shape.setGuardName(newName)

        guard.parent.propertiesBox.setGuardPropertiesBoxTitle(guard.propertiesBox, guard.name)
    }
  }

  /**
    * Removes a guard from a transition
    * @param guard guard to be removed
    * @param transition transition where the guard belongs to
    */
  def removeGuardFromTransition(guard: Guard, transition: Transition): Unit = {
    new RemoveGuardFromTransitionHandler().execute(new RemoveGuardFromTransitionCommand(guard.name, transition.name)) match {
      case Left(error) => println(error.getMessage)
      case Right(_) =>
        transition.removeGuard(guard)

        println("Removing a guard from a transition")
    }
  }

  /**
    * Draws a guard on the canvas
    * @param guard guard to be drawn
    */
  def drawGuard(guard: Guard): Unit = {
    guard.propertiesBox.setGuardName(guard.name)

    guard.shape.setGuardName(guard.name)

    for (action <- guard.actions) {
      ActionController.drawAction(action)
    }

    for (condition <- guard.conditions) {
      ConditionController.drawCondition(condition)
    }

    guard.parent.shape.updateGuardGroupPosition()

    new GuardController(guard)
  }
}
