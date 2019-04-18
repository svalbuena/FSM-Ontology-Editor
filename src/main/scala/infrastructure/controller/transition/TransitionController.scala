package infrastructure.controller.transition

import application.command.state.modify.ModifyStateTypeCommand
import application.command.transition.add.AddTransitionToFsmCommand
import application.command.transition.modify.ModifyTransitionNameCommand
import application.command.transition.remove.RemoveTransitionFromFsmCommand
import application.commandhandler.state.modify.ModifyStateTypeHandler
import application.commandhandler.transition.add.AddTransitionToFsmHandler
import application.commandhandler.transition.modify.ModifyTransitionNameHandler
import application.commandhandler.transition.remove.RemoveTransitionFromFsmHandler
import infrastructure.controller.DrawingPaneController
import infrastructure.controller.guard.GuardController
import infrastructure.element.end.End
import infrastructure.element.start.Start
import infrastructure.element.state.{State, StateType}
import infrastructure.element.transition.Transition
import infrastructure.toolbox.section.selector.mouse.NormalMouseSelector
import javafx.scene.input.MouseButton

class TransitionController(transition: Transition, drawingPaneController: DrawingPaneController) {
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

  private def addGuardToTransition(): Unit = GuardController.addGuardToTransition(transition)
}

object TransitionController {
  def addStartToStateTransition(start: Start, state: State, drawingPaneController: DrawingPaneController): Unit = {
    val (newInfStateType, newAppStateType) = state.stateType match {
      case infrastructure.element.state.StateType.FINAL => (StateType.INITIAL_FINAL, application.command.state.modify.StateType.INITIAL_FINAL)
      case _ => (StateType.INITIAL, application.command.state.modify.StateType.INITIAL)
    }
    new ModifyStateTypeHandler().execute(new ModifyStateTypeCommand(state.name, newAppStateType)) match {
      case Left(error) => println(error.getMessage)
      case Right(_) =>
        val transition = new Transition("startToStateTransition", start, state)

        state.stateType = newInfStateType

        start.outTransitions = transition :: start.outTransitions
        state.inTransitions = transition :: state.inTransitions

        drawTransition(transition, drawingPaneController)
    }
  }

  def addStateToEndTransition(state: State, end: End, drawingPaneController: DrawingPaneController): Unit = {
    val (newInfStateType, newAppStateType) = state.stateType match {
      case infrastructure.element.state.StateType.INITIAL => (StateType.INITIAL_FINAL, application.command.state.modify.StateType.INITIAL_FINAL)
      case _ => (StateType.FINAL, application.command.state.modify.StateType.FINAL)
    }

    new ModifyStateTypeHandler().execute(new ModifyStateTypeCommand(state.name, newAppStateType)) match {
      case Left(error) => println(error.getMessage)
      case Right(_) =>
        val transition = new Transition("stateToEndTransition", state, end)

        state.stateType = newInfStateType

        state.outTransitions = transition :: state.outTransitions
        end.inTransitions = transition :: end.inTransitions

        drawTransition(transition, drawingPaneController)
    }
  }

  def addStateToStateTransitionToFsm(source: State, destination: State, drawingPaneController: DrawingPaneController): Option[Transition] = {
    new AddTransitionToFsmHandler().execute(new AddTransitionToFsmCommand(source.name, destination.name)) match {
      case Left(error) =>
        println(error.getMessage)
        None
      case Right(transitionName) =>
        val transition = new Transition(transitionName, source, destination, isEditable = true)

        source.outTransitions = transition :: source.outTransitions
        destination.inTransitions = transition :: destination.inTransitions

        drawTransition(transition, drawingPaneController)

        Some(transition)
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

  def drawTransition(transition: Transition, drawingPaneController: DrawingPaneController): Unit = {
    drawingPaneController.canvas.drawTransition(transition.shape, transition.getSourceShape, transition.getDestinationShape)

    transition.propertiesBox.setTransitionName(transition.name)

    new TransitionController(transition, drawingPaneController)
  }

  def eraseTransition(transition: Transition, drawingPaneController: DrawingPaneController): Unit = {
    val canvas = drawingPaneController.canvas

    canvas.getChildren.remove(transition.shape)
  }
}
