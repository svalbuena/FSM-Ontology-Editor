package infrastructure.propertybox.state

import infrastructure.elements.action.Action
import infrastructure.elements.node.State
import infrastructure.propertybox.state.section.{ActionsSection, NameSection}
import javafx.event.EventHandler
import javafx.scene.input.KeyEvent
import javafx.scene.layout.{HBox, Pane, VBox}

class StatePropertiesBox(state: State) extends VBox {
  private val nameSection = new NameSection(state.name)
  private val actionsSection = new ActionsSection(state.entryActions, state.exitActions)

  getChildren.addAll(nameSection, actionsSection)

  def updateContent(): Unit = {
    setName(state.name)
    setEntryActions(state.entryActions)
    setExitActions(state.exitActions)
  }

  def setOnNameEditedListener(eventHandler: EventHandler[KeyEvent]): Unit = {
    nameSection.setOnNameEditedListener(eventHandler)
  }

  def getName: String = nameSection.getName
  def setName(name: String): Unit = nameSection.setName(name)
  def setEntryActions(entryActions: List[Action]): Unit = actionsSection.setEntryActions(entryActions)
  def setExitActions(exitActions: List[Action]): Unit = actionsSection.setExitActions(exitActions)
}
