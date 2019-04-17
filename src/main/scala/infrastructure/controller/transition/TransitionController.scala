package infrastructure.controller.transition

import application.command.transition.add.AddTransitionToFsmCommand
import application.command.transition.modify.ModifyTransitionNameCommand
import application.command.transition.remove.RemoveTransitionFromFsmCommand
import application.commandhandler.transition.add.AddTransitionToFsmHandler
import application.commandhandler.transition.modify.ModifyTransitionNameHandler
import application.commandhandler.transition.remove.RemoveTransitionFromFsmHandler
import infrastructure.controller.DrawingPaneController
import infrastructure.controller.guard.GuardController
import infrastructure.element.state.State
import infrastructure.element.transition.Transition
import infrastructure.id.IdGenerator
import infrastructure.toolbox.section.selector.mouse.NormalMouseSelector
import javafx.scene.input.MouseButton

class TransitionController(transition: Transition, drawingPaneController: DrawingPaneController, idGenerator: IdGenerator) {
  private val toolBox = drawingPaneController.toolBox

  private val propertiesBox = transition.propertiesBox
  private val shape = transition.shape

  shape.setOnMouseClicked(event => {
    event.consume()

    event.getButton match {
      case MouseButton.PRIMARY =>
        toolBox.getSelectedTool match {
          case _: NormalMouseSelector =>
            drawingPaneController.propertiesBox.setContent(propertiesBox)
          case _ =>

        }

      case _ =>
    }
  })

  propertiesBox.setOnTransitionNameChanged(newName => TransitionController.modifyTransitionName(transition, newName))

  propertiesBox.setOnAddTransitionGuardButtonClicked(() => addGuardToTransition())

  private def addGuardToTransition(): Unit = GuardController.addGuardToTransition(transition, drawingPaneController)
}

object TransitionController {
  def addTransitionToFsm(source: State, destination: State, drawingPaneController: DrawingPaneController): Unit = {
    new AddTransitionToFsmHandler().execute(new AddTransitionToFsmCommand(source.name, destination.name)) match {
      case Left(error) => println(error.getMessage)
      case Right(transitionName) =>
        val transition = new Transition(transitionName, source, destination)

        drawingPaneController.addTransition(transition)
    }
  }

  def modifyTransitionName(transition: Transition, newName: String): Unit = {
    new ModifyTransitionNameHandler().execute(new ModifyTransitionNameCommand(transition.name, newName)) match {
      case Left(error) => println(error.getMessage)
      case Right(_) =>
        transition.name = newName
        println("Transition name changed to -> " + newName)
    }
  }

  def removeTransitionFromFsm(transition: Transition, drawingPaneController: DrawingPaneController): Unit = {
    new RemoveTransitionFromFsmHandler().execute(new RemoveTransitionFromFsmCommand(transition.name)) match {
      case Left(error) => println(error.getMessage)
      case Right(_) =>
      //TODO: implement RemoveTransitionFromFsm
    }
  }
}
