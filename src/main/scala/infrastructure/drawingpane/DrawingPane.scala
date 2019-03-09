package infrastructure.drawingpane

import infrastructure.drawingpane.DrawingPane.shape
import infrastructure.drawingpane.shape.State
import javafx.event.EventHandler
import javafx.geometry.Point2D
import javafx.scene.Group
import javafx.scene.input.{MouseButton, MouseEvent}
import javafx.scene.layout.Pane
import javafx.scene.shape.{Rectangle, Shape}

object DrawingPane extends Pane {
  var mouseX: Double = 0.0
  var mouseY: Double = 0.0
  var posX: Double = 0.0
  var posY: Double = 0.0

  val shape = State.apply

  drawShape(shape)

  def drawShape(shape: Shape): Unit = {
    shape.setOnMousePressed(mousePressed())
    shape.setOnMouseDragged(mouseDragged())

    getChildren.add(shape)
  }

  private def mousePressed(): EventHandler[MouseEvent] = (event: MouseEvent) => {
    if (event.getButton == MouseButton.PRIMARY) {
      mouseX = event.getSceneX
      mouseY = event.getSceneY
    } else if (event.getButton == MouseButton.SECONDARY) {
      event.getSource match {
        case shape:Shape =>
          println()
          println()
          /*println("--- Layout ---")
          println("\tLayoutX = " + shape.getLayoutX)
          println("\tLayoutY = " + shape.getLayoutY)

          println("--- Bounds In Local ---")
          println("\tCenterX = " + shape.getBoundsInLocal.getCenterX)
          println("\tCenterY = " + shape.getBoundsInLocal.getCenterY)

          println("--- Bounds In Parent ---")
          println("\tCenterX = " + shape.getBoundsInParent.getCenterX)
          println("\tCenterY = " + shape.getBoundsInParent.getCenterY)

          println("--- Layout Bounds ---")
          println("\tCenterX = " + shape.getLayoutBounds.getCenterX)
          println("\tCenterY = " + shape.getLayoutBounds.getCenterY)

          println("--- Size in Local ---")
          println("\tWidth = " + shape.getBoundsInLocal.getWidth)
          println("\tHeight = " + shape.getBoundsInLocal.getHeight)

          println("--- Size in Parent ---")
          println("\tWidth = " + shape.getBoundsInParent.getWidth)
          println("\tHeight = " + shape.getBoundsInParent.getHeight)

          println("--- Size in Layout ---")
          println("\tWidth = " + shape.getLayoutBounds.getWidth)
          println("\tHeight = " + shape.getLayoutBounds.getHeight)*/

          shape.setTranslateX(0)
          shape.setTranslateY(0)

          printShapePosition(shape)
      }
    }
  }

  private def mouseDragged(): EventHandler[MouseEvent] = (event: MouseEvent) => {
    val source = event.getSource
    source match {
      case shape: Shape =>
        val deltaX = event.getSceneX - mouseX
        val deltaY = event.getSceneY - mouseY

        val shapeBounds = shape.getBoundsInParent

        val shapeX = shape.getTranslateX
        val shapeY = shape.getTranslateY

        val newX = shapeX + deltaX
        val newY = shapeY + deltaY

        if (shape.getParent.getLayoutBounds.contains(newX, newY, shapeBounds.getWidth, shapeBounds.getHeight)) {
          shape.setTranslateX(newX)
          shape.setTranslateY(newY)
        }
      case _ => println("hola")
    }

    mouseX = event.getSceneX
    mouseY = event.getSceneY
  }

  def printShapePosition(shape: Shape) = {
    def shapeBounds = shape.getBoundsInParent

    def shapeX = shapeBounds.getCenterX - shapeBounds.getWidth/2
    def shapeY = shapeBounds.getCenterY - shapeBounds.getHeight/2

    println("--- Shape Position ---")
    println("ShapeX = " + shapeX)
    println("ShapeY = " + shapeY)

    println("--- Layout Position ---")
    println("LayoutX = " + shape.getLayoutX)
    println("LayoutY = " + shape.getLayoutY)
  }

  def getShapePosition(shape: Shape): (Double, Double) = {
    def shapeBounds = shape.getBoundsInParent

    def shapeX = shapeBounds.getCenterX - shapeBounds.getWidth/2
    def shapeY = shapeBounds.getCenterY - shapeBounds.getHeight/2

    (shapeX, shapeY)
  }

  def getShapeSize(shape: Shape): (Double, Double) = {
    def shapeBounds = shape.getBoundsInParent

    (shapeBounds.getWidth, shapeBounds.getHeight)
  }
}
