import infrastructure.controller.DrawingPaneController
import infrastructure.drawingpane.DrawingPane
import infrastructure.propertybox.PropertiesBox
import infrastructure.toolbar.MainToolBar
import infrastructure.toolbox.ToolBox
import infrastructure.viewbar.ViewBar
import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.layout.BorderPane
import javafx.stage.Stage

class MainApplication extends Application {
  val SceneWidth = 800
  val SceneHeight = 600

  val CanvasWidth = 4000
  val CanvasHeight = 3000

  val (toolBarWidthProportion, toolBarHeightProportion) = (1.0, 0.08)
  val (viewBarWidthProportion, viewBarHeightProportion) = (1.0, 0.08)

  val (toolBoxWidthProportion, toolBoxHeightProportion) = (0.10, 0.8)
  val (propertiesBoxWidthProportion, propertiesBoxHeightProportion) = (0.15, 0.8)

  val (drawingPaneWidthProportion, drawingPaneHeightProportion) = (1.0 - toolBoxWidthProportion - propertiesBoxWidthProportion, 1.0 - toolBarHeightProportion - viewBarHeightProportion)


  override def start(stage: Stage): Unit = {
    stage.setTitle("This is the title")

    val borderPane = new BorderPane()

    //Creating the main Scene
    val scene = new Scene(borderPane, SceneWidth, SceneHeight)
    scene.getStylesheets.add("infrastructure/css/drawingpane.css")

    //Adding the ToolBar
    val mainToolBar = new MainToolBar
    mainToolBar.prefWidthProperty().bind(scene.widthProperty().multiply(toolBarWidthProportion))
    mainToolBar.prefHeightProperty().bind(scene.heightProperty().multiply(toolBarHeightProportion))
    borderPane.setTop(mainToolBar)

    //Adding the ViewBar
    val viewBar = new ViewBar
    viewBar.prefWidthProperty().bind(scene.widthProperty().multiply(viewBarWidthProportion))
    viewBar.prefHeightProperty().bind(scene.heightProperty().multiply(viewBarHeightProportion))
    borderPane.setBottom(viewBar)

    //Adding the ToolBox
    val toolBox = new ToolBox
    toolBox.prefWidthProperty().bind(scene.widthProperty().multiply(toolBoxWidthProportion))
    toolBox.prefHeightProperty().bind(scene.heightProperty().multiply(toolBoxHeightProportion))
    borderPane.setLeft(toolBox)

    //Adding the PropertiesBox
    val propertiesBox = new PropertiesBox
    propertiesBox.prefWidthProperty().bind(scene.widthProperty().multiply(propertiesBoxWidthProportion))
    propertiesBox.prefHeightProperty().bind(scene.heightProperty().multiply(propertiesBoxHeightProportion))
    borderPane.setRight(propertiesBox)

    //Adding the DrawingPane
    val drawingPane = new DrawingPane(CanvasWidth, CanvasHeight)
    drawingPane.prefWidthProperty().bind(scene.widthProperty().multiply(drawingPaneWidthProportion))
    drawingPane.prefHeightProperty().bind(scene.heightProperty().multiply(drawingPaneHeightProportion))

    borderPane.setCenter(drawingPane)

    //Create the DrawingPaneController
    val infrastrucutreController = new DrawingPaneController(drawingPane, toolBox, propertiesBox)

    stage.setScene(scene)
    stage.show()
  }
}