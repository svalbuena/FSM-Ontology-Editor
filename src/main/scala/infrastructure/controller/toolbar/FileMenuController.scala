package infrastructure.controller.toolbar

import infrastructure.controller.{DrawingPaneController, MainController}
import infrastructure.toolbar.item.FileMenu

class FileMenuController(fileMenu: FileMenu, mainController: MainController) {
  fileMenu.newMenuItem.setOnAction(_ => onNewButtonClicked())
  fileMenu.openMenuItem.setOnAction(_ => onOpenButtonClicked())
  fileMenu.saveMenuItem.setOnAction(_ => onSaveButtonClicked())
  fileMenu.saveAsMenuItem.setOnAction(_ => onSaveAsButtonClicked())


  def setSaveButtonDisable(disable: Boolean): Unit = {
    fileMenu.saveMenuItem.setDisable(disable)
  }

  private def onNewButtonClicked(): Unit = {
    mainController.newFsm()
  }


  private def onOpenButtonClicked(): Unit = {
    mainController.loadFsm()
  }

  private def onSaveButtonClicked(): Unit = {
    mainController.saveFsm()
  }

  private def onSaveAsButtonClicked(): Unit = {
    mainController.saveAsFsm()
  }
}
