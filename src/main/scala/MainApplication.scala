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
  val Width = 400
  val Height = 300

  override def start(stage: Stage): Unit = {
    stage.setTitle("This is the title")

    val layout = new BorderPane()

    //Adding the ToolBar
    MainToolBar.setPrefSize(Width * 1.0, Height * 0.1)
    layout.setTop(MainToolBar)

    //Adding the ViewBar
    ViewBar.setPrefSize(Width * 1.0, Height * 0.1)
    layout.setBottom(ViewBar)

    //Adding the ToolBox
    ToolBox.setPrefSize(Width * 0.20, Height * 0.8)
    layout.setLeft(ToolBox)

    //Adding the PropertiesBox
    PropertiesBox.setPrefSize(Width * 0.20, Height * 0.8)
    layout.setRight(PropertiesBox)

    //Adding the Canvas
    DrawingPane.setPrefSize(Width * 0.6, Height * 0.8)
    layout.setCenter(DrawingPane)

    //Creating the main Scene
    val scene = new Scene(layout, Width, Height)
    stage.setScene(scene)

    stage.show()
  }
}