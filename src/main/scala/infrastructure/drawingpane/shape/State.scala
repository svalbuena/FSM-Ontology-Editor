package infrastructure.drawingpane.shape

import javafx.beans.binding.Bindings
import javafx.geometry.{Insets, Pos}
import javafx.scene.Node
import javafx.scene.control.{Label, TextField}
import javafx.scene.layout.{HBox, Pane, StackPane, VBox}
import javafx.scene.paint.{Color, Paint}
import javafx.scene.shape.{Line, Rectangle, Shape, StrokeType}


// TODO Move shape and text area
class State extends VBox with ConnectableNode {
  private val ActionHeight = 25.0
  private val Width = 250.0
  private val Height = ActionHeight * 4

  private val TitleAreaHeightRatio = 0.20
  private val ActionsAreaHeightRatio = 1 - TitleAreaHeightRatio

  setPrefSize(Width, Height)
  getStyleClass.add("state")

  private val titleArea = createTitleArea()
  titleArea.prefHeightProperty.bind(heightProperty.multiply(TitleAreaHeightRatio))

  private val actionsArea = createActionsArea()
  actionsArea.prefHeightProperty.bind(heightProperty.multiply(ActionsAreaHeightRatio))

  getChildren.addAll(titleArea, actionsArea)

  def addAction(actionMsg: String): Unit = {
    val action = new TextField()
    action.setPrefHeight(ActionHeight)
    //action.setStyle("-fx-background-color: yellow")
    action.setText(actionMsg)

    actionsArea.getChildren.add(action)
  }

  private def createTitleArea(): Pane = {
    val Padding = 10.0

    val pane = new StackPane()
    pane.getStyleClass.add("title-area")
    pane.setPadding(new Insets(Padding))

    val title = new TextField()
    title.setAlignment(Pos.CENTER)
    title.setText("State")

    title.prefWidthProperty.bind(pane.widthProperty)
    title.prefHeightProperty.bind(pane.heightProperty)

    pane.getChildren.add(title)

    pane
  }

  private def createActionsArea(): VBox = {
    val Padding = 10.0

    val pane = new VBox()
    pane.getStyleClass.add("actions-area")
    pane.setPadding(new Insets(Padding))

    pane
  }
}
