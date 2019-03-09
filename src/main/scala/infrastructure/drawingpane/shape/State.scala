package infrastructure.drawingpane.shape

import javafx.scene.paint.{Color, Paint}
import javafx.scene.shape.{Line, Rectangle, Shape}


object State {

  def apply: Shape = {
    val stateX = 0
    val stateY = 0
    val stateWidth = 40
    val stateHeight = 20
    val separatorY = stateY + 8


    val rectangle = new Rectangle(stateX, stateY, stateWidth, stateHeight)
    rectangle.setFill(Color.GREY)
    rectangle.setStroke(Color.GREY)

    val separator = new Line(stateX, separatorY, stateX + stateWidth, separatorY)
    separator.setStroke(Color.BLACK)

    val shape = Shape.union(rectangle, separator)
    shape.setTranslateX(10)
    shape.setTranslateY(10)

    shape
  }
}
