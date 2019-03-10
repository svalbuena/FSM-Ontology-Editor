import infrastructure.drawingpane.DrawingPane
import javafx.application.Application
import javafx.scene.{Node, Scene}
import javafx.scene.control.{Button, MenuButton, ToolBar}
import javafx.scene.layout.{BorderPane, GridPane, StackPane, VBox}
import javafx.stage.Stage
import infrastructure.propertybox.PropertiesBox
import infrastructure.toolbar.MainToolBar
import infrastructure.toolbox.ToolBox
import infrastructure.viewbar.ViewBar

class MainApplication extends Application {
  val Width = 800
  val Height = 600

  override def start(stage: Stage): Unit = {
    stage.setTitle("This is the title")

    val layout = new BorderPane()

    //Adding the ToolBar
    val mainToolBar = new MainToolBar
    mainToolBar.setPrefSize(Width * 1.0, Height * 0.1)
    layout.setTop(mainToolBar)

    //Adding the ViewBar
    val viewBar = new ViewBar
    viewBar.setPrefSize(Width * 1.0, Height * 0.1)
    layout.setBottom(viewBar)

    //Adding the ToolBox
    val toolBox = new ToolBox
    toolBox.setPrefSize(Width * 0.20, Height * 0.8)
    layout.setLeft(toolBox)

    //Adding the PropertiesBox
    val propertiesBox = new PropertiesBox
    propertiesBox.setPrefSize(Width * 0.20, Height * 0.8)
    layout.setRight(propertiesBox)

    //Adding the DrawingPane
    val drawingPane = new DrawingPane(toolBox)
    drawingPane.setPrefSize(Width * 0.6, Height * 0.8)
    layout.setCenter(drawingPane)

    //Creating the main Scene
    val scene = new Scene(layout, Width, Height)
    stage.setScene(scene)

    stage.show()
  }
}