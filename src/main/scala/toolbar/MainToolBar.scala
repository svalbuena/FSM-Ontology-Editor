package toolbar

import javafx.scene.control.ToolBar
import toolbar.items.FileMenuButton

object MainToolBar extends ToolBar {
  getItems.add(FileMenuButton)
}
