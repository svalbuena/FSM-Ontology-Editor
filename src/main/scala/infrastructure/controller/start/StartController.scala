package infrastructure.controller.start

import application.command.start.add.AddStartToFsmCommand
import application.command.start.remove.RemoveStartFromFsmCommand
import application.commandhandler.start.add.AddStartToFsmHandler
import application.commandhandler.start.remove.RemoveStartFromFsmHandler
import infrastructure.controller.DrawingPaneController
import infrastructure.drawingpane.DrawingPane
import infrastructure.element.start.Start
import infrastructure.id.IdGenerator
import infrastructure.toolbox.section.item.fsm.TransitionItem
import infrastructure.toolbox.section.selector.mouse.{DeleteMouseSelector, NormalMouseSelector}
import javafx.scene.input.MouseButton

class StartController(start: Start, drawingPaneController: DrawingPaneController, drawingPane: DrawingPane, idGenerator: IdGenerator) {
  private val toolBox = drawingPaneController.toolBox
  private val propertiesBox = drawingPaneController.propertiesBox
  private val canvas = drawingPane.canvas

  private val startShape = start.shape

  startShape.setOnMouseClicked(event => {
    event.consume()

    event.getButton match {
      case MouseButton.PRIMARY =>
        toolBox.getSelectedTool match {
          case _: TransitionItem =>
            if (drawingPaneController.isTemporalTransitionDefined) {
              drawingPaneController.establishTemporalTransition(start)
              toolBox.setToolToDefault()
            } else {
              val point = startShape.getLocalToParentTransform.transform(event.getX, event.getY)
              drawingPaneController.addTemporalTransition(start, point.getX, point.getY)
            }

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
        canvas.dragConnectableNode(startShape, deltaX, deltaY)
        start.getTransitions.foreach(transition => canvas.dragTransition(transition.shape, transition.getSourceShape, transition.getDestinationShape))

      case _ =>
    }

    drawingPaneController.updateMousePosition(event)
  })

  def removeStart(): Unit = {
    StartController.removeStartFromFsm(start, drawingPaneController)
  }
}

object StartController {
  def addStartToFsm(x: Double, y: Double, drawingPaneController: DrawingPaneController): Unit = {
    new AddStartToFsmHandler().execute(new AddStartToFsmCommand) match {
      case Left(error) => println(error.getMessage)
      case Right(_) =>
        val start = new Start("start")

        drawingPaneController.addStart(start, x, y)

        println("Adding a start")
    }
  }

  def removeStartFromFsm(start: Start, drawingPaneController: DrawingPaneController): Unit = {
    new RemoveStartFromFsmHandler().execute(new RemoveStartFromFsmCommand) match {
      case Left(error) => println(error.getMessage)
      case Right(_) =>
        drawingPaneController.removeConnectableElement(start, start.shape)

        println("Removing start")
    }
  }
}