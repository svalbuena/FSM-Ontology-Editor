package infrastructure

import infrastructure.controller.MainController
import infrastructure.drawingpane.DrawingPane
import infrastructure.propertybox.PropertiesBox
import infrastructure.toolbar.MainToolBar
import infrastructure.toolbox.ToolBox
import infrastructure.viewbar.ViewBar
import javafx.application.Application
import javafx.geometry.Dimension2D
import javafx.scene.Scene
import javafx.scene.layout.BorderPane
import javafx.stage.Stage


class MainApplication extends Application {
  val SceneDimension = new Dimension2D(1000, 600)

  val ToolBarDimension = new Dimension2D(0, 15)
  val ViewBarDimension = new Dimension2D(0, 15)

  val ToolBoxDimension = new Dimension2D(200, 0)
  val PropertiesBoxDimension = new Dimension2D(250, 0)

  val CanvasDimension = new Dimension2D(4000, 3000)


  override def start(stage: Stage): Unit = {
    stage.setTitle("This is the title")

    val borderPane = new BorderPane()

    //Creating the main Scene
    val scene = new Scene(borderPane, SceneDimension.getWidth, SceneDimension.getHeight)
    scene.getStylesheets.add("infrastructure/css/drawingpane.css")

    //Adding the ToolBar
    val mainToolBar = new MainToolBar
    mainToolBar.prefWidthProperty().bind(scene.widthProperty())
    mainToolBar.setPrefHeight(ToolBarDimension.getHeight)
    borderPane.setTop(mainToolBar)

    //Adding the ViewBar
    val viewBar = new ViewBar
    viewBar.prefWidthProperty().bind(scene.widthProperty())
    viewBar.setPrefHeight(ViewBarDimension.getHeight)
    borderPane.setBottom(viewBar)

    //Adding the ToolBox
    val toolBox = new ToolBox
    toolBox.setPrefWidth(ToolBoxDimension.getWidth)
    toolBox.prefHeightProperty().bind(scene.heightProperty().subtract(mainToolBar.heightProperty()).subtract(viewBar.heightProperty()))
    borderPane.setLeft(toolBox)

    //Adding the PropertiesBox
    val propertiesBox = new PropertiesBox
    propertiesBox.setPrefWidth(PropertiesBoxDimension.getWidth)
    propertiesBox.prefHeightProperty().bind(scene.heightProperty().subtract(mainToolBar.heightProperty()).subtract(viewBar.heightProperty()))
    borderPane.setRight(propertiesBox)

    //Adding the DrawingPane
    val drawingPane = new DrawingPane(CanvasDimension.getWidth, CanvasDimension.getHeight)
    drawingPane.prefWidthProperty().bind(scene.widthProperty().subtract(toolBox.widthProperty()).subtract(propertiesBox.widthProperty()))
    drawingPane.prefHeightProperty().bind(scene.heightProperty().subtract(mainToolBar.heightProperty().subtract(viewBar.heightProperty())))
    borderPane.setCenter(drawingPane)

    val mainController = new MainController(stage, drawingPane, toolBox, propertiesBox, mainToolBar.fileMenu)

    stage.setScene(scene)
    stage.show()
  }
}