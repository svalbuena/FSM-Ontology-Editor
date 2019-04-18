package infrastructure.controller.start

import application.command.state.modify.ModifyStateTypeCommand
import application.commandhandler.state.modify.ModifyStateTypeHandler
import infrastructure.controller.DrawingPaneController
import infrastructure.controller.transition.TransitionController
import infrastructure.element.start.Start
import infrastructure.element.state.{State, StateType}
import infrastructure.toolbox.section.item.fsm.TransitionItem
import infrastructure.toolbox.section.selector.mouse.{DeleteMouseSelector, NormalMouseSelector}
import javafx.scene.input.MouseButton

class StartController(start: Start, drawingPaneController: DrawingPaneController) {
  private val toolBox = drawingPaneController.toolBox
  private val propertiesBox = drawingPaneController.propertiesBox
  private val canvas = drawingPaneController.canvas

  private val startShape = start.shape

  startShape.setOnMouseClicked(event => {
    event.consume()

    event.getButton match {
      case MouseButton.PRIMARY =>
        toolBox.getSelectedTool match {
          case _: TransitionItem =>
            val point = startShape.getLocalToParentTransform.transform(event.getX, event.getY)
            drawingPaneController.transitionToolUsed(start, point)

          case _: DeleteMouseSelector =>
            removeStart()
            toolBox.setToolToDefault()

          case _ =>
        }

      case _ =>
    }
  })

  startShape.setOnMouseDragged(event => {
    event.consume()

    toolBox.getSelectedTool match {
      case _: NormalMouseSelector =>
        val (deltaX, deltaY) = drawingPaneController.calculateDeltaFromMouseEvent(event)
        canvas.moveConnectableNode(startShape, deltaX, deltaY)
        start.getTransitions.foreach(transition => canvas.moveTransition(transition.shape, transition.getSourceShape, transition.getDestinationShape))

      case _ =>
    }

    drawingPaneController.updateMousePosition(event)
  })

  private def removeStart(): Unit = {
    StartController.removeStart(start, drawingPaneController)
  }
}

object StartController {
  def addStart(x: Double, y: Double, drawingPaneController: DrawingPaneController): Unit = {
    val start = new Start("start")

    drawStart(start, x, y, drawingPaneController)
  }

  def removeStart(start: Start, drawingPaneController: DrawingPaneController): Unit = {
    var ok = true

    for (startOutTransition <- start.outTransitions) {
      startOutTransition.source match {
        case state: State =>
          val (newInfStateType, newAppStateType) = state.stateType match {
            case infrastructure.element.state.StateType.INITIAL_FINAL => (StateType.INITIAL, application.command.state.modify.StateType.INITIAL)
            case _ => (StateType.SIMPLE, application.command.state.modify.StateType.SIMPLE)
          }
          new ModifyStateTypeHandler().execute(new ModifyStateTypeCommand(state.name, newAppStateType)) match {
            case Left(error) =>
              println(error.getMessage)
              ok = false
            case Right(_) =>
              state.stateType = newInfStateType

              state.inTransitions = state.inTransitions.filterNot(t => t == startOutTransition)
              TransitionController.eraseTransition(startOutTransition, drawingPaneController)
          }

        case _ =>
          startOutTransition.destination.inTransitions = startOutTransition.destination.inTransitions.filterNot(t => t == startOutTransition)
          TransitionController.eraseTransition(startOutTransition, drawingPaneController)
      }
    }

    if (ok) {
      eraseStart(start, drawingPaneController)
    }
  }

  def drawStart(start: Start, x: Double, y: Double, drawingPaneController: DrawingPaneController): Unit = {
    drawingPaneController.canvas.drawConnectableNode(start.shape, x, y)

    new StartController(start, drawingPaneController)
  }

  def eraseStart(start: Start, drawingPaneController: DrawingPaneController): Unit = {
    val canvas = drawingPaneController.canvas

    canvas.getChildren.remove(start.shape)
  }

}