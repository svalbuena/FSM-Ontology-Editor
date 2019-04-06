package infrastructure.drawingpane

import infrastructure.elements.node.GhostElement
import infrastructure.elements.transition.Transition
import infrastructure.propertybox.PropertiesBox
import infrastructure.toolbox.ToolBox
import javafx.scene.control.ScrollPane
import javafx.scene.input.MouseEvent

class DrawingPane(canvasWidth: Double, canvasHeight: Double) extends ScrollPane {
  val canvas = new Canvas()
  canvas.setPrefSize(canvasWidth, canvasHeight)
  canvas.setMinSize(canvasWidth, canvasHeight)
  canvas.setMaxSize(canvasWidth, canvasHeight)

  setContent(canvas)
}
