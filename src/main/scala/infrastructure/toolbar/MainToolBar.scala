package infrastructure.toolbar

import javafx.scene.control.ToolBar
import infrastructure.toolbar.items.FileMenuButton
import infrastructure.viewbar.ViewBar.setStyle

object MainToolBar extends ToolBar {
  setStyle("-fx-background-color: #b3bfc6")

  getItems.add(FileMenuButton)
}
