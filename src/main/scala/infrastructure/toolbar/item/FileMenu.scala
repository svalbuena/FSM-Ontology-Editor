package infrastructure.toolbar.item

import javafx.scene.control.{MenuButton, MenuItem}

object FileMenuButton extends MenuButton {
  setText("File")

  val openMenuItem = new MenuItem()
  openMenuItem.setText("Open")

  val saveMenuItem = new MenuItem()
  saveMenuItem.setText("Save")

  getItems.addAll(openMenuItem, saveMenuItem)
}
