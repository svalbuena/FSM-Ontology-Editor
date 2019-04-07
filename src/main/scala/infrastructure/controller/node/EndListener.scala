package infrastructure.controller.node

import infrastructure.controller.DrawingPaneController
import infrastructure.drawingpane.DrawingPane
import infrastructure.elements.node.End
import infrastructure.toolbox.section.item.fsm.TransitionItem
import infrastructure.toolbox.section.selector.mouse.{DeleteMouseSelector, NormalMouseSelector}
import javafx.scene.input.MouseButton

class EndListener(end: End, infrastructureController: DrawingPaneController, drawingPane: DrawingPane) {
  private val toolBox = infrastructureController.toolBox
  private val propertiesBox = infrastructureController.propertiesBox
  private val canvas = drawingPane.canvas

  private val endShape = end.shape

  endShape.setOnMouseClicked(event => {
    event.consume()

    event.getButton match {
      case MouseButton.PRIMARY =>
        toolBox.getSelectedTool match {
          case _: TransitionItem =>
            if (infrastructureController.isTemporalTransitionDefined) {
              infrastructureController.establishTemporalTransition(end)
              toolBox.setToolToDefault()
            } else {
              val point = endShape.getLocalToParentTransform.transform(event.getX, event.getY)
              infrastructureController.addTemporalTransition(end, point.getX, point.getY)
            }

          case _: DeleteMouseSelector =>
            infrastructureController.removeConnectableElement(end, endShape)
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
        val (deltaX, deltaY) = infrastructureController.calculateDeltaFromMouseEvent(event)
        canvas.dragConnectableNode(endShape, deltaX, deltaY)
        end.getTransitions.foreach(transition => canvas.dragTransition(transition.shape, transition.getSourceShape, transition.getDestinationShape))

      case _ =>

    }

    infrastructureController.updateMousePosition(event)
  })
}
