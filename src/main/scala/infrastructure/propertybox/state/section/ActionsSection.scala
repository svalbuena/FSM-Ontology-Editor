package infrastructure.propertybox.state.section

import infrastructure.elements.action.Action
import infrastructure.propertybox.action.ActionPropertiesBox
import javafx.scene.control.ScrollPane.ScrollBarPolicy
import javafx.scene.control.{Label, ScrollPane, TextField}
import javafx.scene.layout.{HBox, Pane, VBox}

class ActionsSection(entryActions: List[Action], exitActions: List[Action]) extends VBox {
  private val titleLabel = new Label()
  titleLabel.setText("Actions")

  private val entryActionsSection = new VBox()
  private val exitActionsSection = new VBox()

  private val actionsSection = new VBox()
  actionsSection.getChildren.addAll(entryActionsSection, exitActionsSection)

  getChildren.addAll(titleLabel, actionsSection)

  setEntryActions(entryActions)
  setExitActions(exitActions)

  def setEntryActions(entryActions: List[Action]): Unit = setActionsOnSection(entryActions, entryActionsSection)

  def setExitActions(exitActions: List[Action]): Unit = setActionsOnSection(exitActions, exitActionsSection)

  def setActionsOnSection(actions: List[Action], section: Pane): Unit = {
    section.getChildren.removeAll(section.getChildren)
    actions.foreach(action => addActionToSection(action, section))
  }

  def addActionToSection(action: Action, section: Pane): Unit = {
    val actionSection = new ActionSection(action)

    section.getChildren.add(actionSection)
  }
}
