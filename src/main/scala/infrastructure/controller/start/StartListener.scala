package infrastructure.controller.start

import infrastructure.controller.DrawingPaneController
import infrastructure.drawingpane.DrawingPane
import infrastructure.elements.node.Start
import infrastructure.id.IdGenerator
import infrastructure.toolbox.section.item.fsm.TransitionItem
import infrastructure.toolbox.section.selector.mouse.{DeleteMouseSelector, NormalMouseSelector}
import javafx.scene.input.MouseButton

class StartListener(start: Start, infrastructureController: DrawingPaneController, drawingPane: DrawingPane, idGenerator: IdGenerator) {
  private val toolBox = infrastructureController.toolBox
  private val propertiesBox = infrastructureController.propertiesBox
  private val canvas = drawingPane.canvas

  private val startShape = start.shape

  startShape.setOnMouseClicked(event => {
    event.consume()

    event.getButton match {
      case MouseButton.PRIMARY =>
        toolBox.getSelectedTool match {
          case _: TransitionItem =>
            if (infrastructureController.isTemporalTransitionDefined) {
              infrastructureController.establishTemporalTransition(start)
              toolBox.setToolToDefault()
            } else {
              val point = startShape.getLocalToParentTransform.transform(event.getX, event.getY)
              infrastructureController.addTemporalTransition(start, point.getX, point.getY)
            }

          case _: DeleteMouseSelector =>
            infrastructureController.removeConnectableElement(start, startShape)
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
        val (deltaX, deltaY) = infrastructureController.calculateDeltaFromMouseEvent(event)
        canvas.dragConnectableNode(startShape, deltaX, deltaY)
        start.getTransitions.foreach(transition => canvas.dragTransition(transition.shape, transition.getSourceShape, transition.getDestinationShape))

      case _ =>
    }

    infrastructureController.updateMousePosition(event)
  })
}
