package infrastructure.drawingpane.shape

import javafx.beans.binding.Bindings
import javafx.geometry.Insets
import javafx.scene.Node
import javafx.scene.control.TextField
import javafx.scene.layout.{Pane, VBox}
import javafx.scene.paint.{Color, Paint}
import javafx.scene.shape.{Line, Rectangle, Shape, StrokeType}


// TODO Move shape and text area
class State(width: Double = 400.0, height: Double = 300.0, var id: Int = 0) extends VBox with ConnectableNode {
  private val SeparatorOffset: Double = 0.20
  private val SeparatorY = height * SeparatorOffset
  private val StrokeWidth: Double = 2.0
  val title = new TextField()


  val titleArea = createTitleArea()
  private val actionsArea = createActionsArea()


  //setPrefSize(width, height)

  //titleArea.prefHeightProperty.bind(prefHeightProperty().multiply(0.2))
  titleArea.setPrefHeight(height)
  titleArea.setPrefWidth(width)
  //actionsArea.setPrefHeight(100)

  getChildren.addAll(titleArea, actionsArea)
  //setPrefWidth(width)

  //getChildren.add(createShape())
  //getChildren.add(createTitle())
  getStyleClass.add("state" + id)

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
    val Padding = 10
    val pane = new Pane()

    title.setText("State")
    //title.setPadding(new Insets(Padding, Padding, Padding, Padding))

    title.prefWidthProperty.bind(pane.widthProperty.subtract(Padding * 2.0))
    title.prefHeightProperty.bind(pane.heightProperty.subtract(Padding * 2.0))

    title.layoutXProperty.bind(pane.widthProperty.subtract(title.widthProperty).divide(2))
    title.layoutYProperty.bind(pane.heightProperty.subtract(title.heightProperty).divide(2))

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
