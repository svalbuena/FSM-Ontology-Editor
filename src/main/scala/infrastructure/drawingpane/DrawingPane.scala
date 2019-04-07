package infrastructure.drawingpane

import javafx.scene.control.ScrollPane

class DrawingPane(canvasWidth: Double, canvasHeight: Double) extends ScrollPane {
  val canvas = new Canvas()
  canvas.setPrefSize(canvasWidth, canvasHeight)
  canvas.setMinSize(canvasWidth, canvasHeight)
  canvas.setMaxSize(canvasWidth, canvasHeight)

  setContent(canvas)
}
