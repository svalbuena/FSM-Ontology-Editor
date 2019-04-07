package infrastructure.toolbar

import infrastructure.toolbar.item.FileMenuButton
import javafx.scene.control.ToolBar

class MainToolBar extends ToolBar {
  setStyle("-fx-background-color: #b3bfc6")

  getItems.add(FileMenuButton)
}
