package infrastructure.drawingpane.shape

import javafx.scene.control.TextField
import javafx.scene.layout.Pane
import javafx.scene.paint.{Color, Paint}
import javafx.scene.shape.{Line, Rectangle, Shape, StrokeType}


class State(width: Double, height: Double) extends Pane {
  private val SeparatorOffset: Double = 0.20
  private val SeparatorY = height * SeparatorOffset
  private val StrokeWidth:Double = 2.0

  var transitionList: List[Transition] = List[Transition]()

  getChildren.add(createShape())
  getChildren.add(createTitle())

  setStyle("-fx-background-color: #f4f4f4")

  def addTransition(transition: Transition): Unit = {
    transitionList = transition :: transitionList
  }

  def getCenterCoordinates: (Double, Double) = {
    val bounds = getBoundsInParent

    (bounds.getCenterX, bounds.getCenterY)
  }

  private def createShape(): Shape = {
    val rectangle = new Rectangle(0, 0, width, height)
    rectangle.setFill(null)
    rectangle.setStroke(Color.BLACK)
    rectangle.setStrokeType(StrokeType.INSIDE)
    rectangle.setStrokeWidth(StrokeWidth)

    val separator = new Line(StrokeWidth, SeparatorY, width - StrokeWidth, SeparatorY)
    separator.setStrokeWidth(StrokeWidth)

    Shape.union(rectangle, separator)
  }

  private def createTitle(): TextField = {
    val stateTitle = new TextField()

    stateTitle.setText("State")
    stateTitle.setTranslateX(StrokeWidth)
    stateTitle.setTranslateY(StrokeWidth)
    stateTitle.setPrefSize(width - StrokeWidth * 2, SeparatorY - StrokeWidth * 2)

    stateTitle
  }
}
