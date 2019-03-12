package infrastructure.drawingpane.shape

import javafx.scene.control.TextField
import javafx.scene.layout.{Pane, VBox}
import javafx.scene.paint.{Color, Paint}
import javafx.scene.shape.{Line, Rectangle, Shape, StrokeType}


class State(width: Double = 200.0, height: Double = 150.0) extends ConnectableNode {
  private val SeparatorOffset: Double = 0.20
  private val SeparatorY = height * SeparatorOffset
  private val StrokeWidth:Double = 2.0
  private val titleArea = new Pane()
  private val actionsArea = new Pane()
  private val mainPane = new VBox()

  setPrefSize(width, height)
  getChildren.add(mainPane)
  mainPane.getChildren.addAll(titleArea, actionsArea)
  
  getChildren.add(createShape())
  getChildren.add(createTitle())

  setStyle("-fx-background-color: #f4f4f4")

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

  private def addEntryAction(): Unit = {
    val textField = new TextField()
    textField.setText("do/Action")

  }
}
