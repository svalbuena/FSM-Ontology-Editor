package infrastructure.drawingpane

import javafx.event.EventHandler
import javafx.geometry.Point2D
import javafx.scene.Group
import javafx.scene.input.{MouseButton, MouseEvent}
import javafx.scene.layout.Pane
import javafx.scene.shape.{Rectangle, Shape}

object DrawingPane extends Pane {
  var mouseX: Double = 0.0
  var mouseY: Double = 0.0

  val rectangle = new Rectangle(20, 0, 20, 30)
  drawShape(rectangle)

  def drawShape(shape: Shape): Unit = {
    shape.setOnMousePressed(mousePressed())
    shape.setOnMouseDragged(mouseDragged())

    getChildren.add(shape)
  }

  private def mousePressed(): EventHandler[MouseEvent] = (event: MouseEvent) => {
    if (event.getButton == MouseButton.PRIMARY) {
      mouseX = event.getSceneX
      mouseY = event.getSceneY
    }
  }

  private def mouseDragged(): EventHandler[MouseEvent] = (event: MouseEvent) => {
    val source = event.getSource
    source match {
      case shape: Shape =>
        val deltaX = event.getSceneX - mouseX
        val deltaY = event.getSceneY - mouseY

        val shapeBounds = shape.getLayoutBounds
        val parentBounds = shape.getParent.getLayoutBounds

        val pos = shape.localToParent(Point2D.ZERO)

        val shapeX = pos.getX
        val shapeY = pos.getY

        val newX = shapeX + deltaX
        val newY = shapeY + deltaY

        if (parentBounds.contains(newX, newY, shapeBounds.getWidth, shapeBounds.getHeight)) {
          shape.setLayoutX(newX)
          shape.setLayoutY(newY)
          println("Shape X: " + shapeX)
          println("Shape Y: " + shapeY)
        }
      case _ => println("hola")
    }

    mouseX = event.getSceneX
    mouseY = event.getSceneY
  }
}
