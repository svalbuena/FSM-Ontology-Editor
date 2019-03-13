package infrastructure.drawingpane.shape

import javafx.beans.binding.Bindings
import javafx.geometry.Insets
import javafx.scene.Node
import javafx.scene.control.TextField
import javafx.scene.layout.{Pane, VBox}
import javafx.scene.paint.{Color, Paint}
import javafx.scene.shape.{Line, Rectangle, Shape, StrokeType}


// TODO Move shape and text area
class State(width: Double = 200.0, height: Double = 150.0) extends VBox with ConnectableNode {
  private val SeparatorOffset: Double = 0.20
  private val SeparatorY = height * SeparatorOffset
  private val StrokeWidth:Double = 2.0

  private val titleArea = createTitleArea()
  private val actionsArea = createActionsArea()

  setPrefSize(width, height)
  getChildren.addAll(titleArea, actionsArea)
  
  //getChildren.add(createShape())
  //getChildren.add(createTitle())

  getStyleClass.add("state")
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

  private def createTitleArea(): Pane = {
    val Padding = 2.0
    val title = new TextField()
    title.setText("State")
    title.setPadding(new Insets(Padding, Padding, Padding, Padding))
    title.prefWidthProperty.bind(Bindings.subtract(prefWidthProperty, Padding * 4))
    //title.setTranslateX(StrokeWidth)
    //title.setTranslateY(StrokeWidth)
    val pane = new Pane()
    pane.getChildren.add(title)

    pane
  }

  private def createActionsArea(): VBox = {
    val pane = new VBox()

    pane
  }

  private def addEntryAction(): Unit = {
    val textField = new TextField()
    textField.setText("do/Action")

  }
}
