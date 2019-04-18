package infrastructure.controller.end

import application.command.state.modify.ModifyStateTypeCommand
import application.commandhandler.state.modify.ModifyStateTypeHandler
import infrastructure.controller.DrawingPaneController
import infrastructure.controller.transition.TransitionController
import infrastructure.element.end.End
import infrastructure.element.state.{State, StateType}
import infrastructure.toolbox.section.item.fsm.TransitionItem
import infrastructure.toolbox.section.selector.mouse.{DeleteMouseSelector, NormalMouseSelector}
import javafx.scene.input.MouseButton

class EndController(end: End, drawingPaneController: DrawingPaneController) {
  private val toolBox = drawingPaneController.toolBox
  private val propertiesBox = drawingPaneController.propertiesBox
  private val canvas = drawingPaneController.canvas

  private val endShape = end.shape

  endShape.setOnMouseClicked(event => {
    event.consume()

    event.getButton match {
      case MouseButton.PRIMARY =>
        toolBox.getSelectedTool match {
          case _: TransitionItem =>
            val point = endShape.getLocalToParentTransform.transform(event.getX, event.getY)
            drawingPaneController.transitionToolUsed(end, point)

          case _: DeleteMouseSelector =>
            removeEnd()
            toolBox.setToolToDefault()

          case _ =>

        }

      case _ =>

    }
  })

  endShape.setOnMouseDragged(event => {
    event.consume()

    toolBox.getSelectedTool match {
      case _: NormalMouseSelector =>
        val (deltaX, deltaY) = drawingPaneController.calculateDeltaFromMouseEvent(event)
        canvas.moveConnectableNode(endShape, deltaX, deltaY)
        end.getTransitions.foreach(transition => canvas.moveTransition(transition.shape, transition.getSourceShape, transition.getDestinationShape))

      case _ =>
    }

    drawingPaneController.updateMousePosition(event)
  })

  private def removeEnd(): Unit = EndController.removeEnd(end, drawingPaneController)
}

object EndController {
  def addEnd(x: Double, y: Double, drawingPaneController: DrawingPaneController): Unit = {
    val end = new End("end")

    drawEnd(end, x, y, drawingPaneController)
  }

  def removeEnd(end: End, drawingPaneController: DrawingPaneController): Unit = {
    var ok = true

    for (endInTransition <- end.inTransitions) {
      endInTransition.source match {
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

              state.outTransitions = state.outTransitions.filterNot(t => t == endInTransition)
              TransitionController.eraseTransition(endInTransition, drawingPaneController)
          }

        case _ =>
          endInTransition.source.outTransitions = endInTransition.source.outTransitions.filterNot(t => t == endInTransition)
          TransitionController.eraseTransition(endInTransition, drawingPaneController)
      }
    }

    if (ok) {
      eraseEnd(end, drawingPaneController)
    }
  }

  def drawEnd(end: End, x: Double, y: Double, drawingPaneController: DrawingPaneController): Unit = {
    drawingPaneController.canvas.drawConnectableNode(end.shape, x, y)

    new EndController(end, drawingPaneController)
  }

  def eraseEnd(end: End, drawingPaneController: DrawingPaneController): Unit = {
    val canvas = drawingPaneController.canvas

    canvas.getChildren.remove(end.shape)
  }
}