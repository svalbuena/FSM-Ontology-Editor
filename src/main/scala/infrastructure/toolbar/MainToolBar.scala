package infrastructure.toolbar

import infrastructure.toolbar.item.FileMenu
import javafx.scene.control.ToolBar

class MainToolBar extends ToolBar {
  setStyle("-fx-background-color: #b3bfc6")

  val fileMenu = new FileMenu

  getItems.add(fileMenu)
}
