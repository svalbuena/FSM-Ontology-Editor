import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.control.{Button, MenuButton, ToolBar}
import javafx.scene.layout.{BorderPane, GridPane, StackPane, VBox}
import javafx.stage.Stage
import toolbox.ToolBox

class MainApplication extends Application {
  val Width = 400
  val Height = 300

  override def start(stage: Stage): Unit = {
    stage.setTitle("This is the title")

    val layout = new BorderPane()
    layout.setTop(toolbar.MainToolBar)
    layout.setLeft(ToolBox)

    val scene = new Scene(layout, Width, Height)
    stage.setScene(scene)

    stage.show()
  }
}