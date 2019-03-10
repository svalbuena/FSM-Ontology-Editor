package infrastructure.drawingpane

import infrastructure.drawingpane.shape.{State, Transition}
import javafx.event.EventHandler
import javafx.scene.{Group, Node}
import javafx.scene.input.{MouseButton, MouseEvent}
import javafx.scene.layout.Pane
import javafx.scene.paint.Color
import javafx.scene.shape.{Line, Rectangle, Shape}

object DrawingPane extends Pane {
  var mouseX: Double = 0.0
  var mouseY: Double = 0.0
  var posX: Double = 0.0
  var posY: Double = 0.0

  val state1 = new State(200, 150)
  val state2 = new State(200, 150)

  state2.setTranslateX(300)

  val transition = new Transition(state1, state2)

  drawNode(transition)
  drawNode(state1)
  drawNode(state2)

  state1.addTransition(transition)
  state2.addTransition(transition)

  def drawNode(shape: Node): Unit = {
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
        case node:Node =>
          node.setTranslateX(0)
          node.setTranslateY(0)
      }
    }
  }

  private def mouseDragged(): EventHandler[MouseEvent] = (event: MouseEvent) => {
    val source = event.getSource
    source match {
      case node: State =>
        val deltaX = event.getSceneX - mouseX
        val deltaY = event.getSceneY - mouseY

        val shapeBounds = node.getBoundsInParent

        val shapeX = node.getTranslateX
        val shapeY = node.getTranslateY

        val newX = shapeX + deltaX
        val newY = shapeY + deltaY

        if (node.getParent.getLayoutBounds.contains(newX, newY, shapeBounds.getWidth, shapeBounds.getHeight)) {
          node.setTranslateX(newX)
          node.setTranslateY(newY)

          node.transitionList.foreach(t => {
            val (state1X, state1Y) = state1.getCenterCoordinates
            val (state2X, state2Y) = state2.getCenterCoordinates

            t.moveLine(state1X, state1Y, state2X, state2Y)
          })
        }
      case _ => println("hola")
    }

    mouseX = event.getSceneX
    mouseY = event.getSceneY
  }
}
