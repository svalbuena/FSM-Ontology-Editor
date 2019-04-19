package infrastructure.controller.toolbar

import infrastructure.toolbar.item.FileMenu

class FileMenuController(fileMenu: FileMenu) {
  fileMenu.openMenuItem.setOnAction(_ => onOpenFileButtonClicked())
  fileMenu.saveMenuItem.setOnAction(_ => onSaveFileButtonClicked())


  private def onOpenFileButtonClicked(): Unit = {

  }

  private def onSaveFileButtonClicked(): Unit = {

  }
}
