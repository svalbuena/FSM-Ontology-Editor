package infrastructure.toolbar

import infrastructure.toolbar.item.FileMenu
import javafx.scene.control.ToolBar

/**
  * ToolBar with the program options
  */
class MainToolBar extends ToolBar {
  val fileMenu = new FileMenu

  getItems.add(fileMenu)

  setStyle()


  private def setStyle(): Unit = {
    getStyleClass.add("toolbar")

    fileMenu.getStyleClass.add("filemenu")
  }
}
