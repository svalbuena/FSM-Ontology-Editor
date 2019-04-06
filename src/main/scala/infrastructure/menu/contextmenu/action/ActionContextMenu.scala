package infrastructure.menu.contextmenu.action

import infrastructure.menu.contextmenu.action.item.DeleteActionMenuItem
import javafx.scene.control.ContextMenu

class ActionContextMenu extends ContextMenu {
  val deleteActionMenuItem = new DeleteActionMenuItem

  getItems.add(deleteActionMenuItem)
}
