package infrastructure.controller

import infrastructure.element.start.Start
import infrastructure.toolbox.section.item.fsm.TransitionItem
import infrastructure.toolbox.section.selector.mouse.{DeleteMouseSelector, NormalMouseSelector}
import javafx.scene.input.MouseButton

class StartController(start: Start, drawingPaneController: DrawingPaneController) {
  private val toolBox = drawingPaneController.toolBox
  private val propertiesBox = drawingPaneController.propertiesBox

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
        drawingPaneController.moveNode(startShape, deltaX, deltaY)
        start.getTransitions.foreach(transition => drawingPaneController.moveTransition(transition.shape, transition.getSourceShape, transition.getDestinationShape))

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
    val start = new Start("start", x, y)

    drawStart(start, drawingPaneController)
  }

  def removeStart(start: Start, drawingPaneController: DrawingPaneController): Unit = {
    var success = true

    for (startOutTransition <- start.outTransitions) {
      if (!TransitionController.removeTransitionFromFsm(startOutTransition, drawingPaneController)) {
        success = false
      }
    }

    if (success) {
      eraseStart(start, drawingPaneController)
    }
  }

  def drawStart(start: Start, drawingPaneController: DrawingPaneController): Unit = {
    drawingPaneController.drawNode(start.shape, start.x, start.y)

    new StartController(start, drawingPaneController)
  }

  def eraseStart(start: Start, drawingPaneController: DrawingPaneController): Unit = {
    drawingPaneController.removeNode(start.shape)
  }
}