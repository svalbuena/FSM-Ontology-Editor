import infrastructure.InfrastructureController
import infrastructure.drawingpane.DrawingPane
import javafx.application.Application
import javafx.scene.{Group, Scene}
import javafx.scene.layout.{BorderPane, Pane, Region}
import javafx.stage.Stage
import infrastructure.propertybox.PropertiesBox
import infrastructure.toolbar.MainToolBar
import infrastructure.toolbox.ToolBox
import infrastructure.viewbar.ViewBar
import javafx.css.Size
import javafx.scene.control.ScrollPane

class MainApplication extends Application {
  val SceneWidth = 800
  val SceneHeight = 600

  val DrawingPaneWidth = 4000
  val DrawingPaneHeight = 3000

  val (toolBarWidthProportion, toolBarHeightProportion) = (1.0, 0.08)
  val (viewBarWidthProportion, viewBarHeightProportion) = (1.0, 0.08)

  val (toolBoxWidthProportion, toolBoxHeightProportion) = (0.10, 0.8)
  val (propertiesBoxWidthProportion, propertiesBoxHeightProportion) = (0.15, 0.8)

  val (drawingPaneContainerWidthProportion, drawingPaneContainerHeightProportion) = (1.0 - toolBoxWidthProportion - propertiesBoxWidthProportion, 1.0 - toolBarHeightProportion - viewBarHeightProportion)


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

    //Adding the DrawingPaneContainer
    val drawingPane = new DrawingPane()
    drawingPane.setPrefSize(DrawingPaneWidth, DrawingPaneHeight)
    drawingPane.setMinSize(DrawingPaneWidth, DrawingPaneHeight)
    drawingPane.setMaxSize(DrawingPaneWidth, DrawingPaneHeight)



    val drawingPaneContainer = new ScrollPane()
    drawingPaneContainer.setContent(drawingPane)
    drawingPaneContainer.prefWidthProperty().bind(scene.widthProperty().multiply(drawingPaneContainerWidthProportion))
    drawingPaneContainer.prefHeightProperty().bind(scene.heightProperty().multiply(drawingPaneContainerHeightProportion))

    borderPane.setCenter(drawingPaneContainer)

    //Create the DrawingPaneController
    val infrastrucutreController = new InfrastructureController(drawingPane, toolBox, propertiesBox)

    stage.setScene(scene)
    stage.show()

    println("--- Drawing Pane ---")
    println("Width = " + drawingPane.getWidth)
    println("Height = " + drawingPane.getHeight)
    println("BoundsWidth = " + drawingPane.getLayoutBounds.getWidth)
    println("BoundsHeight = " + drawingPane.getLayoutBounds.getHeight)

  }
}