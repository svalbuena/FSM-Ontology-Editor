package infrastructure.controller

import infrastructure.toolbar.item.FileMenu

/**
  * Controls the buttons of the file menu
  *
  * @param fileMenu       file menu to control
  * @param mainController main controller of the application
  */
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
