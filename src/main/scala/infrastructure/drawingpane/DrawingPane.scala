package infrastructure.drawingpane

import javafx.scene.control.ScrollPane

/**
  * It is the scrollpane that contains the canvas
  * @param canvasWidth width of the canvas
  * @param canvasHeight height of the canvas
  */
class DrawingPane(canvasWidth: Double, canvasHeight: Double) extends ScrollPane {
  val canvas = new Canvas()
  canvas.setPrefSize(canvasWidth, canvasHeight)
  canvas.setMinSize(canvasWidth, canvasHeight)
  canvas.setMaxSize(canvasWidth, canvasHeight)

  setContent(canvas)
}
