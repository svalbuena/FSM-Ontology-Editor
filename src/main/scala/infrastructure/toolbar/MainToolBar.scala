package infrastructure.toolbar

import javafx.scene.control.ToolBar
import infrastructure.toolbar.item.FileMenuButton
import infrastructure.viewbar.ViewBar.setStyle

object MainToolBar extends ToolBar {
  setStyle("-fx-background-color: #b3bfc6")

  getItems.add(FileMenuButton)
}
