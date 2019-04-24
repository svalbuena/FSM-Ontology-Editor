package infrastructure.controller

import infrastructure.element.end.End
import infrastructure.toolbox.section.item.fsm.TransitionItem
import infrastructure.toolbox.section.selector.mouse.{DeleteMouseSelector, NormalMouseSelector}
import javafx.scene.input.MouseButton

class EndController(end: End, drawingPaneController: DrawingPaneController) {
  private val toolBox = drawingPaneController.toolBox
  private val propertiesBox = drawingPaneController.propertiesBox

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
        drawingPaneController.moveNode(endShape, deltaX, deltaY)
        end.getTransitions.foreach(transition => drawingPaneController.moveTransition(transition.shape, transition.getSourceShape, transition.getDestinationShape))

      case _ =>
    }

    drawingPaneController.updateMousePosition(event)
  })

  private def removeEnd(): Unit = EndController.removeEnd(end, drawingPaneController)
}

object EndController {
  def addEnd(x: Double, y: Double, drawingPaneController: DrawingPaneController): Unit = {
    val end = new End("end", x, y)

    drawEnd(end, drawingPaneController)
  }

  def removeEnd(end: End, drawingPaneController: DrawingPaneController): Unit = {
    var success = true

    for (endInTransition <- end.inTransitions) {
      if (!TransitionController.removeTransitionFromFsm(endInTransition, drawingPaneController)) {
        success = false
      }
    }

    if (success) {
      eraseEnd(end, drawingPaneController)
    }
  }

  def drawEnd(end: End, drawingPaneController: DrawingPaneController): Unit = {
    drawingPaneController.drawNode(end.shape, end.x, end.y)

    new EndController(end, drawingPaneController)
  }

  def eraseEnd(end: End, drawingPaneController: DrawingPaneController): Unit = {
    drawingPaneController.removeNode(end.shape)
  }
}