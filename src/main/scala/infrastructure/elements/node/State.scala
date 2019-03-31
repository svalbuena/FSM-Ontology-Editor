package infrastructure.elements.node

import infrastructure.drawingpane.shape.state.StateShape
import infrastructure.elements.action.{EntryAction, ExitAction}
import infrastructure.menu.contextmenu.state.StateContextMenu
import infrastructure.propertybox.state.StatePropertiesBox

class State(var name: String = "State", var entryActions: List[EntryAction] = List(), var exitActions: List[ExitAction] = List()) extends ConnectableElement {
  val shape = new StateShape(this)
  val propertiesBox = new StatePropertiesBox(this)
  val contextMenu = new StateContextMenu

  def setName(name: String): Unit = {
    this.name = name
    shape.setName(name)
    propertiesBox.setName(name)
  }

  def updateContent(): Unit = {
    shape.updateContent()
    propertiesBox.updateContent()
  }

  def addEntryAction(): Unit = {
    val entryAction = new EntryAction("Action")

    entryActions = entryAction :: entryActions

    updateContent()
  }

  def addExitAction(): Unit = {
    val exitAction = new ExitAction("Action")

    exitActions = exitAction :: exitActions

    updateContent()
  }
}
