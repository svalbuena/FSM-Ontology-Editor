package infrastructure.controller

import application.command.state.add.AddStateToFsmCommand
import application.command.state.modify.{ModifyStateCoordinatesCommand, ModifyStateNameCommand, ModifyStateTypeCommand}
import application.command.state.remove.RemoveStateFromFsmCommand
import application.commandhandler.state.add.AddStateToFsmHandler
import application.commandhandler.state.modify.{ModifyStateCoordinatesHandler, ModifyStateNameHandler, ModifyStateTypeHandler}
import application.commandhandler.state.remove.RemoveStateFromFsmHandler
import infrastructure.element.action.ActionType
import infrastructure.element.end.End
import infrastructure.element.fsm.FiniteStateMachine
import infrastructure.element.start.Start
import infrastructure.element.state.StateType.StateType
import infrastructure.element.state.{State, StateType}
import infrastructure.element.transition.Transition
import infrastructure.main.EnvironmentSingleton
import infrastructure.menu.contextmenu.state.item.{AddEntryActionMenuItem, AddExitActionMenuItem}
import infrastructure.toolbox.section.item.fsm.TransitionItem
import infrastructure.toolbox.section.selector.mouse.{DeleteMouseSelector, NormalMouseSelector}
import javafx.scene.input.MouseButton

/**
  * Controls the visual and behavior aspects of a State
  *
  * @param state                 state to control
  * @param drawingPaneController controller of the drawing pane
  */
class StateController(state: State, drawingPaneController: DrawingPaneController) {
  private val toolBox = drawingPaneController.toolBox

  private val shape = state.shape
  private val propertiesBox = state.propertiesBox
  private val contextMenu = state.contextMenu

  shape.setOnMouseClicked(event => {
    event.consume()

    event.getButton match {
      case MouseButton.PRIMARY =>
        toolBox.getSelectedTool match {
          case _: NormalMouseSelector =>
            drawingPaneController.propertiesBox.setOtherPropertiesBoxContent(propertiesBox)

          case _: TransitionItem =>
            val point = state.shape.getLocalToParentTransform.transform(event.getX, event.getY)
            drawingPaneController.transitionToolUsed(state, point)

          case _: DeleteMouseSelector =>
            StateController.removeStateFromFsm(state, drawingPaneController)
            drawingPaneController.propertiesBox.removeOtherPropertiesBoxContentIfEqual(state.propertiesBox)
            toolBox.setToolToDefault()

          case _ =>

        }
      case _ =>
    }
  })

  shape.setOnMouseDragged(event => {
    event.consume()

    toolBox.getSelectedTool match {
      case _: NormalMouseSelector =>
        val (deltaX, deltaY) = drawingPaneController.calculateDeltaFromMouseEvent(event)
        val newPosition = drawingPaneController.moveNode(shape, deltaX, deltaY)
        state.getTransitions.foreach(transition => drawingPaneController.moveTransition(transition.shape, transition.getSourceShape, transition.getDestinationShape))
        StateController.modifyStateCoordinates(state, newPosition.getX, newPosition.getY)

      case _ =>
    }

    drawingPaneController.updateMousePosition(event)
  })

  shape.setOnContextMenuRequested(event => {
    event.consume()

    contextMenu.show(shape, event.getScreenX, event.getScreenY)
  })

  contextMenu.getItems.forEach {
    case menuItem: AddEntryActionMenuItem => menuItem.setOnAction(_ => addExitActionToState())
    case menuItem: AddExitActionMenuItem => menuItem.setOnAction(_ => addExitActionToState())
  }

  propertiesBox.setOnStateNameChanged(newName => StateController.modifyStateName(state, newName))
  propertiesBox.setOnAddEntryActionButtonClicked(() => addEntryActionToState())
  propertiesBox.setOnAddExitActionButtonClicked(() => addExitActionToState())

  private def addEntryActionToState(): Unit = ActionController.addActionToState(ActionType.ENTRY, state, drawingPaneController)

  private def addExitActionToState(): Unit = ActionController.addActionToState(ActionType.EXIT, state, drawingPaneController)
}

/**
  * Operations that can be done with a State
  */
object StateController {
  private val environment = EnvironmentSingleton.get()

  /**
    * Creates a state and adds it to an fsm
    *
    * @param x                     x position
    * @param y                     y position
    * @param fsm                   fsm where the state will be added
    * @param drawingPaneController controller of the drawing pane
    * @return the state if its creation was successful
    */
  def addStateToFsm(x: Double, y: Double, fsm: FiniteStateMachine, drawingPaneController: DrawingPaneController): Option[State] = {
    new AddStateToFsmHandler(environment).execute(new AddStateToFsmCommand(x, y)) match {
      case Left(error) =>
        println(error.getMessage)
        None
      case Right(stateName) =>
        val state = new State(stateName, x, y, StateType.SIMPLE, parent = fsm)

        fsm.addState(state)

        drawState(state, drawingPaneController)

        println("Adding a state")
        Some(state)
    }
  }

  /**
    * Draws a state on the canvas
    *
    * @param state                 state to be drawn
    * @param drawingPaneController controller of the drawing pane
    */
  def drawState(state: State, drawingPaneController: DrawingPaneController): Unit = {
    state.propertiesBox.setName(state.name)

    state.shape.setName(state.name)
    drawingPaneController.drawNode(state.shape, state.x, state.y)

    for (action <- state.actions) {
      ActionController.drawAction(action)
    }

    new StateController(state, drawingPaneController)

    if (state.stateType == StateType.INITIAL || state.stateType == StateType.INITIAL_FINAL) {
      val start = new Start("start", state.x, state.y)
      StartController.drawStart(start, drawingPaneController)

      val transition = new Transition("startToStateTransition", start, state, isEditable = false, state.parent)
      start.outTransitions = transition :: start.outTransitions
      state.inTransitions = transition :: state.inTransitions
      TransitionController.drawTransition(transition, drawingPaneController)
    }

    if (state.stateType == StateType.FINAL || state.stateType == StateType.INITIAL_FINAL) {
      val end = new End("start", state.x, state.y)
      EndController.drawEnd(end, drawingPaneController)

      val transition = new Transition("stateToEndTransition", state, end, isEditable = false, state.parent)
      state.outTransitions = transition :: state.outTransitions
      end.inTransitions = transition :: end.inTransitions
      TransitionController.drawTransition(transition, drawingPaneController)
    }
  }

  /**
    * Modifies the coordinates of a state
    *
    * @param state state to be modified
    * @param newX  new x
    * @param newY  new y
    */
  def modifyStateCoordinates(state: State, newX: Double, newY: Double): Unit = {
    new ModifyStateCoordinatesHandler(environment).execute(new ModifyStateCoordinatesCommand(state.name, newX, newY)) match {
      case Left(error) => println(error.getMessage)
      case Right(_) =>
        state.x = newX
        state.y = newY
    }
  }

  /**
    * Modifies the name of a state
    *
    * @param state   state to be modified
    * @param newName new name
    */
  def modifyStateName(state: State, newName: String): Unit = {
    new ModifyStateNameHandler(environment).execute(new ModifyStateNameCommand(state.name, newName)) match {
      case Left(error) => println(error.getMessage)
      case Right(_) =>
        state.name = newName

        state.shape.setName(newName)

        println("State name changed to -> " + newName)
    }
  }

  /**
    * Modifies the type of a state
    *
    * @param state        state to be modified
    * @param newStateType new type
    */
  def modifyStateType(state: State, newStateType: StateType): Unit = {
    new ModifyStateTypeHandler(environment).execute(new ModifyStateTypeCommand(state.name, newStateType match {
      case infrastructure.element.state.StateType.INITIAL => application.command.state.modify.StateType.INITIAL
      case infrastructure.element.state.StateType.SIMPLE => application.command.state.modify.StateType.SIMPLE
      case infrastructure.element.state.StateType.FINAL => application.command.state.modify.StateType.FINAL
    })) match {
      case Left(error) => println(error.getMessage)
      case Right(_) =>
        state.stateType = newStateType

        println("State type changed to -> " + newStateType)
    }
  }

  /**
    * Removes a state from an fsm
    *
    * @param state                 state to be removed
    * @param drawingPaneController controller of the drawing pane
    */
  def removeStateFromFsm(state: State, drawingPaneController: DrawingPaneController): Unit = {
    new RemoveStateFromFsmHandler(environment).execute(new RemoveStateFromFsmCommand(state.name)) match {
      case Left(error) => println(error.getMessage)
      case Right(_) =>
        for (transition <- state.inTransitions ::: state.outTransitions) {
          TransitionController.removeTransitionFromFsm(transition, drawingPaneController)
        }

        state.parent.removeState(state)

        eraseState(state, drawingPaneController)

        drawingPaneController.propertiesBox.removeOtherPropertiesBoxContentIfEqual(state.propertiesBox)

        println("Removing a state")
    }
  }

  /**
    * Erases a state from the canvas
    *
    * @param state                 state to be erased
    * @param drawingPaneController controller of the drawing pane
    */
  def eraseState(state: State, drawingPaneController: DrawingPaneController): Unit = {
    drawingPaneController.removeNode(state.shape)
  }
}
