package infrastructure.controller.state

import application.command.state.add.AddStateToFsmCommand
import application.command.state.modify.{ModifyStateNameCommand, ModifyStateTypeCommand}
import application.command.state.remove.RemoveStateFromFsmCommand
import application.commandhandler.state.add.AddStateToFsmHandler
import application.commandhandler.state.modify.{ModifyStateNameHandler, ModifyStateTypeHandler}
import application.commandhandler.state.remove.RemoveStateFromFsmHandler
import infrastructure.controller.DrawingPaneController
import infrastructure.controller.action.ActionController
import infrastructure.controller.transition.TransitionController
import infrastructure.element.action.ActionType
import infrastructure.element.fsm.FiniteStateMachine
import infrastructure.element.state.StateType.StateType
import infrastructure.element.state.{State, StateType}
import infrastructure.menu.contextmenu.state.item.{AddEntryActionMenuItem, AddExitActionMenuItem}
import infrastructure.toolbox.section.item.fsm.TransitionItem
import infrastructure.toolbox.section.selector.mouse.{DeleteMouseSelector, NormalMouseSelector}
import javafx.scene.input.MouseButton

class StateController(state: State, drawingPaneController: DrawingPaneController) {
  private val toolBox = drawingPaneController.toolBox
  private val canvas = drawingPaneController.canvas

  private val shape = state.shape
  private val propertiesBox = state.propertiesBox
  private val contextMenu = state.contextMenu

  shape.setOnMouseClicked(event => {
    event.consume()

    event.getButton match {
      case MouseButton.PRIMARY =>
        toolBox.getSelectedTool match {
          case _: NormalMouseSelector =>
            drawingPaneController.propertiesBox.setContent(propertiesBox)

          case _: TransitionItem =>
            val point = state.shape.getLocalToParentTransform.transform(event.getX, event.getY)
            drawingPaneController.transitionToolUsed(state, point)

          case _: DeleteMouseSelector =>
            StateController.removeStateFromFsm(state, drawingPaneController)
            drawingPaneController.propertiesBox.removeContent()
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
        canvas.moveConnectableNode(shape, deltaX, deltaY)
        state.getTransitions.foreach(transition => canvas.moveTransition(transition.shape, transition.getSourceShape, transition.getDestinationShape))

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

object StateController {
  def addStateToFsm(x: Double, y: Double, fsm: FiniteStateMachine,drawingPaneController: DrawingPaneController): Option[State] = {
    new AddStateToFsmHandler().execute(new AddStateToFsmCommand(x, y)) match {
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

  def modifyStateName(state: State, newName: String): Unit = {
    new ModifyStateNameHandler().execute(new ModifyStateNameCommand(state.name, newName)) match {
      case Left(error) => println(error.getMessage)
      case Right(_) =>
        state.shape.setName(newName)

        println("State name changed to -> " + newName)
    }
  }

  def modifyStateType(state: State, newStateType: StateType): Unit = {
    new ModifyStateTypeHandler().execute(new ModifyStateTypeCommand(state.name, newStateType match {
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

  def removeStateFromFsm(state: State, drawingPaneController: DrawingPaneController): Unit = {
    new RemoveStateFromFsmHandler().execute(new RemoveStateFromFsmCommand(state.name)) match {
      case Left(error) => println(error.getMessage)
      case Right(_) =>
        for (transition <- state.inTransitions ::: state.outTransitions) {
          TransitionController.removeTransitionFromFsm(transition, drawingPaneController)
        }

        state.parent.removeState(state)

        eraseState(state, drawingPaneController)

        drawingPaneController.propertiesBox.removeContentIfEqual(state.propertiesBox)

        println("Removing a state")
    }
  }

  def drawState(state: State, drawingPaneController: DrawingPaneController): Unit = {
    state.propertiesBox.setName(state.name)

    state.shape.setName(state.name)
    drawingPaneController.canvas.drawConnectableNode(state.shape, state.x, state.y)

    for (action <- state.actions) {
      ActionController.drawAction(action)
    }

    new StateController(state, drawingPaneController)
  }

  def eraseState(state: State, drawingPaneController: DrawingPaneController): Unit = {
    val canvas = drawingPaneController.canvas

    canvas.getChildren.remove(state.shape)
  }
}
