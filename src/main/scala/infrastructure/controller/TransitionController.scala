package infrastructure.controller

import application.command.state.modify.ModifyStateTypeCommand
import application.command.transition.add.AddTransitionToFsmCommand
import application.command.transition.modify.ModifyTransitionNameCommand
import application.command.transition.remove.RemoveTransitionFromFsmCommand
import application.commandhandler.state.modify.ModifyStateTypeHandler
import application.commandhandler.transition.add.AddTransitionToFsmHandler
import application.commandhandler.transition.modify.ModifyTransitionNameHandler
import application.commandhandler.transition.remove.RemoveTransitionFromFsmHandler
import infrastructure.EnvironmentSingleton
import infrastructure.element.end.End
import infrastructure.element.fsm.FiniteStateMachine
import infrastructure.element.start.Start
import infrastructure.element.state.{State, StateType}
import infrastructure.element.transition.Transition
import infrastructure.toolbox.section.selector.mouse.{DeleteMouseSelector, NormalMouseSelector}
import javafx.scene.input.MouseButton

/**
  * Controls the visual and behavior aspects of a transition
  *
  * @param transition            transition to control
  * @param drawingPaneController controller of the drawing pane
  */
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
            if (transition.isEditable) {
              drawingPaneController.propertiesBox.setOtherPropertiesBoxContent(propertiesBox)
            }

          case _: DeleteMouseSelector =>
            TransitionController.removeTransitionFromFsm(transition, drawingPaneController)
            drawingPaneController.propertiesBox.removeOtherPropertiesBoxContentIfEqual(propertiesBox)

          case _ =>

        }

      case _ =>
    }
  })

  propertiesBox.setOnTransitionNameChanged(newName => TransitionController.modifyTransitionName(transition, newName))

  propertiesBox.setOnAddTransitionGuardButtonClicked(() => addGuardToTransition())

  private def addGuardToTransition(): Unit = GuardController.addGuardToTransition(transition)
}

/**
  * Operations that can be done with a Transition
  */
object TransitionController {
  private val environment = EnvironmentSingleton.get()

  /**
    * Creates a transition from a start to a state and adds it to an fsm
    *
    * @param start                 source
    * @param state                 destination
    * @param fsm                   fsm where the transition will be added
    * @param drawingPaneController controller of the drawing pane
    */
  def addStartToStateTransition(start: Start, state: State, fsm: FiniteStateMachine, drawingPaneController: DrawingPaneController): Unit = {
    val (newInfStateType, newAppStateType) = state.stateType match {
      case infrastructure.element.state.StateType.FINAL => (StateType.INITIAL_FINAL, application.command.state.modify.StateType.INITIAL_FINAL)
      case _ => (StateType.INITIAL, application.command.state.modify.StateType.INITIAL)
    }
    new ModifyStateTypeHandler(environment).execute(new ModifyStateTypeCommand(state.name, newAppStateType)) match {
      case Left(error) => println(error.getMessage)
      case Right(_) =>
        val transition = new Transition("startToStateTransition", start, state, parent = fsm)

        state.stateType = newInfStateType

        start.outTransitions = transition :: start.outTransitions
        state.inTransitions = transition :: state.inTransitions

        drawTransition(transition, drawingPaneController)
    }
  }

  /**
    * Creates a transition from a state to an end and adds it to an fsm
    *
    * @param state                 source
    * @param end                   destination
    * @param fsm                   fsm where the transition will be added
    * @param drawingPaneController controller of the drawing pane
    */
  def addStateToEndTransition(state: State, end: End, fsm: FiniteStateMachine, drawingPaneController: DrawingPaneController): Unit = {
    val (newInfStateType, newAppStateType) = state.stateType match {
      case infrastructure.element.state.StateType.INITIAL => (StateType.INITIAL_FINAL, application.command.state.modify.StateType.INITIAL_FINAL)
      case _ => (StateType.FINAL, application.command.state.modify.StateType.FINAL)
    }

    new ModifyStateTypeHandler(environment).execute(new ModifyStateTypeCommand(state.name, newAppStateType)) match {
      case Left(error) => println(error.getMessage)
      case Right(_) =>
        val transition = new Transition("stateToEndTransition", state, end, parent = fsm)

        state.stateType = newInfStateType

        state.outTransitions = transition :: state.outTransitions
        end.inTransitions = transition :: end.inTransitions

        drawTransition(transition, drawingPaneController)
    }
  }

  /**
    * Creates a transition from a state to a state and adds it to an fsm
    *
    * @param source                source
    * @param destination           destination
    * @param fsm                   fsm where the transition will be added
    * @param drawingPaneController controller of the drawing pane
    */
  def addStateToStateTransitionToFsm(source: State, destination: State, fsm: FiniteStateMachine, drawingPaneController: DrawingPaneController): Option[Transition] = {
    new AddTransitionToFsmHandler(environment).execute(new AddTransitionToFsmCommand(source.name, destination.name)) match {
      case Left(error) =>
        println(error.getMessage)
        None
      case Right(transitionName) =>
        val transition = new Transition(transitionName, source, destination, isEditable = true, parent = fsm)

        fsm.addTransition(transition)

        source.outTransitions = transition :: source.outTransitions
        destination.inTransitions = transition :: destination.inTransitions

        drawTransition(transition, drawingPaneController)

        Some(transition)
    }
  }

  /**
    * Draws a transition in the canvas
    *
    * @param transition            transition to be drawn
    * @param drawingPaneController controller of the drawing pane
    */
  def drawTransition(transition: Transition, drawingPaneController: DrawingPaneController): Unit = {
    drawingPaneController.drawTransition(transition.shape, transition.getSourceShape, transition.getDestinationShape)

    transition.propertiesBox.setTransitionName(transition.name)

    for (guard <- transition.guards) {
      GuardController.drawGuard(guard)
    }

    new TransitionController(transition, drawingPaneController)
  }

  /**
    * Modifies the name of a transition
    *
    * @param transition transition to be modified
    * @param newName    new name
    */
  def modifyTransitionName(transition: Transition, newName: String): Unit = {
    new ModifyTransitionNameHandler(environment).execute(new ModifyTransitionNameCommand(transition.name, newName)) match {
      case Left(error) => println(error.getMessage)
      case Right(_) =>
        transition.name = newName
        println("Transition name changed to -> " + newName)
    }
  }

  /**
    * Removes a transition from an fsm
    *
    * @param transition            transition to be removed
    * @param drawingPaneController controller of the drawing pane
    * @return true if the transition could be removed successfully, false otherwise
    */
  def removeTransitionFromFsm(transition: Transition, drawingPaneController: DrawingPaneController): Boolean = {
    var success = false

    (transition.source, transition.destination) match {
      case (_: Start, state: State) =>
        val (newInfStateType, newAppStateType) = state.stateType match {
          case infrastructure.element.state.StateType.INITIAL_FINAL => (StateType.FINAL, application.command.state.modify.StateType.FINAL)
          case _ => (StateType.SIMPLE, application.command.state.modify.StateType.SIMPLE)
        }
        new ModifyStateTypeHandler(environment).execute(new ModifyStateTypeCommand(state.name, newAppStateType)) match {
          case Left(error) => println(error.getMessage)
          case Right(_) =>
            state.stateType = newInfStateType
            success = true
        }

      case (state: State, _: End) =>
        val (newInfStateType, newAppStateType) = state.stateType match {
          case infrastructure.element.state.StateType.INITIAL_FINAL => (StateType.INITIAL, application.command.state.modify.StateType.INITIAL)
          case _ => (StateType.SIMPLE, application.command.state.modify.StateType.SIMPLE)
        }
        new ModifyStateTypeHandler(environment).execute(new ModifyStateTypeCommand(state.name, newAppStateType)) match {
          case Left(error) => println(error.getMessage)
          case Right(_) =>
            state.stateType = newInfStateType
            success = true
        }

      case (_: State, _: State) =>
        new RemoveTransitionFromFsmHandler(environment).execute(new RemoveTransitionFromFsmCommand(transition.name)) match {
          case Left(error) => println(error.getMessage)
          case Right(_) =>
            success = true
        }

      case (_, _) =>
    }

    if (success) {
      transition.source.outTransitions = transition.source.outTransitions.filterNot(t => t == transition)
      transition.destination.inTransitions = transition.destination.inTransitions.filterNot(t => t == transition)

      transition.parent.removeTransition(transition)

      eraseTransition(transition, drawingPaneController)

      drawingPaneController.propertiesBox.removeOtherPropertiesBoxContentIfEqual(transition.propertiesBox)
    }

    success
  }

  /**
    * Erase a transition from the canvas
    *
    * @param transition            transition to be erased
    * @param drawingPaneController controller of the drawing pane
    */
  def eraseTransition(transition: Transition, drawingPaneController: DrawingPaneController): Unit = {
    drawingPaneController.removeNode(transition.shape)
  }
}
