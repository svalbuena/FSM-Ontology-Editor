package infrastructure.menu.contextmenu.state

import infrastructure.menu.contextmenu.state.item.{AddEntryActionMenuItem, AddExitActionMenuItem}
import javafx.scene.control.ContextMenu

/**
  * Context menu of a state
  */
class StateContextMenu extends ContextMenu {
  val addEntryActionMenuItem = new AddEntryActionMenuItem
  val addExitActionMenuItem = new AddExitActionMenuItem

  getItems.addAll(addEntryActionMenuItem, addExitActionMenuItem)
}
