package drawingpane

import javafx.scene.canvas.{Canvas, GraphicsContext}
import javafx.scene.paint.Color

object MainCanvas extends Canvas {
  val gc: GraphicsContext = getGraphicsContext2D
  gc.setFill(Color.BLUE)
  gc.fillRect(0.0, 0.0, 10.0, 10.0)

}
