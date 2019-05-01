package infrastructure.toolbar.item

import javafx.scene.control.{MenuButton, MenuItem}

/**
  * Contains the buttons that provide the fsm files managing
  */
class FileMenu extends MenuButton {
  setText("File")

  val newMenuItem = new MenuItem()
  newMenuItem.setText("New")

  val openMenuItem = new MenuItem()
  openMenuItem.setText("Open")

  val saveMenuItem = new MenuItem()
  saveMenuItem.setText("Save")

  val saveAsMenuItem = new MenuItem()
  saveAsMenuItem.setText("Save as")


  getItems.addAll(newMenuItem, openMenuItem, saveMenuItem, saveAsMenuItem)
}
