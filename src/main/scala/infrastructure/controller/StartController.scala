package infrastructure.controller

import infrastructure.element.start.Start
import infrastructure.toolbox.section.item.fsm.TransitionItem
import infrastructure.toolbox.section.selector.mouse.{DeleteMouseSelector, NormalMouseSelector}
import javafx.scene.input.MouseButton

/**
  * Controls the visual and behavior aspects of a Start
  * @param start start to control
  * @param drawingPaneController controller of the drawing pane
  */
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

/**
  * Operations that can be done with a Start
  */
object StartController {

  /**
    * Creates a start
    * @param x x position
    * @param y y position
    * @param drawingPaneController controller of the drawing pane
    */
  def addStart(x: Double, y: Double, drawingPaneController: DrawingPaneController): Unit = {
    val start = new Start("start", x, y)

    drawStart(start, drawingPaneController)
  }

  /**
    * Removes a start
    * @param start start to be removed
    * @param drawingPaneController controller of the drawing pane
    */
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

  /**
    * Draws a start on the canvas
    * @param start start to be drawn
    * @param drawingPaneController controller of the drawing pane
    */
  def drawStart(start: Start, drawingPaneController: DrawingPaneController): Unit = {
    drawingPaneController.drawNode(start.shape, start.x, start.y)

    new StartController(start, drawingPaneController)
  }

  /**
    * Erases a start from the canvas
    * @param start start to be erased
    * @param drawingPaneController controller of the drawing pane
    */
  def eraseStart(start: Start, drawingPaneController: DrawingPaneController): Unit = {
    drawingPaneController.removeNode(start.shape)
  }
}