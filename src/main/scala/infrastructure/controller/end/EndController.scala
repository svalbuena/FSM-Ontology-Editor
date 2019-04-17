package infrastructure.controller.end

import application.command.end.add.AddEndToFsmCommand
import application.command.end.remove.RemoveEndFromFsmCommand
import application.commandhandler.end.add.AddEndToFsmHandler
import application.commandhandler.end.remove.RemoveEndFromFsmHandler
import infrastructure.controller.DrawingPaneController
import infrastructure.drawingpane.DrawingPane
import infrastructure.element.end.End
import infrastructure.toolbox.section.item.fsm.TransitionItem
import infrastructure.toolbox.section.selector.mouse.{DeleteMouseSelector, NormalMouseSelector}
import javafx.scene.input.MouseButton

class EndController(end: End, drawingPaneController: DrawingPaneController, drawingPane: DrawingPane) {
  private val toolBox = drawingPaneController.toolBox
  private val propertiesBox = drawingPaneController.propertiesBox
  private val canvas = drawingPane.canvas

  private val endShape = end.shape

  endShape.setOnMouseClicked(event => {
    event.consume()

    event.getButton match {
      case MouseButton.PRIMARY =>
        toolBox.getSelectedTool match {
          case _: TransitionItem =>
            if (drawingPaneController.isTemporalTransitionDefined) {
              drawingPaneController.establishTemporalTransition(end)
              toolBox.setToolToDefault()
            } else {
              val point = endShape.getLocalToParentTransform.transform(event.getX, event.getY)
              drawingPaneController.addTemporalTransition(end, point.getX, point.getY)
            }

          case _: DeleteMouseSelector =>
            EndController.removeEndFromFsm(end, drawingPaneController)
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
        canvas.dragConnectableNode(endShape, deltaX, deltaY)
        end.getTransitions.foreach(transition => canvas.dragTransition(transition.shape, transition.getSourceShape, transition.getDestinationShape))

      case _ =>

    }

    drawingPaneController.updateMousePosition(event)
  })
}

object EndController {
  def addEndToFsm(x: Double, y: Double, drawingPaneController: DrawingPaneController): Unit = {
    new AddEndToFsmHandler().execute(new AddEndToFsmCommand) match {
      case Left(error) => println(error.getMessage)
      case Right(_) =>
        val end = new End("end")

        drawingPaneController.addEnd(end, x, y)

        println("Adding a end")
    }
  }

  def removeEndFromFsm(end: End, drawingPaneController: DrawingPaneController): Unit = {
    new RemoveEndFromFsmHandler().execute(new RemoveEndFromFsmCommand) match {
      case Left(error) => println(error.getMessage)
      case Right(_) =>
        drawingPaneController.removeConnectableElement(end, end.shape)

        println("Removing end")
    }
  }
}