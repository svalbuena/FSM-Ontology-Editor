package toolbar

import javafx.scene.control.ToolBar
import toolbar.items.FileMenuButton
import viewbar.ViewBar.setStyle

object MainToolBar extends ToolBar {
  setStyle("-fx-background-color: #b3bfc6")

  getItems.add(FileMenuButton)
}
