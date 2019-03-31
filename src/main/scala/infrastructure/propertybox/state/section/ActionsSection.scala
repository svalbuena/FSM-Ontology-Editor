package infrastructure.propertybox.state.section

import infrastructure.elements.action.Action
import javafx.scene.control.{Label, ScrollPane, TextField}
import javafx.scene.layout.{HBox, Pane, VBox}

class ActionsSection(entryActions: List[Action], exitActions: List[Action]) extends VBox {
  private val titleLabel = new Label()
  titleLabel.setText("Actions")

  private val entryActionsSection = new VBox()
  private val exitActionsSection = new VBox()

  private val actionsSection = new VBox()
  actionsSection.getChildren.addAll(entryActionsSection, exitActionsSection)

  private val actionScrollPane = new ScrollPane()
  actionScrollPane.setContent(actionsSection)

  getChildren.addAll(titleLabel, actionScrollPane)

  setEntryActions(entryActions)
  setExitActions(exitActions)

  def setEntryActions(entryActions: List[Action]): Unit = {
    entryActionsSection.getChildren.removeAll(entryActionsSection.getChildren)
    entryActions.foreach(entryAction => addActionToSection(entryAction.id, "entry/", entryAction.text, entryActionsSection))
  }

  def setExitActions(exitActions: List[Action]): Unit = {
    exitActionsSection.getChildren.removeAll(exitActionsSection.getChildren)
    exitActions.foreach(exitAction => addActionToSection(exitAction.id, "exit/", exitAction.text, exitActionsSection))
  }

  private def addActionToSection(id: String, actionType: String, actionText: String, section: Pane): Unit = {
    val actionPane = new HBox()

    val actionTypeLabel = new Label()
    actionTypeLabel.setText(actionType)

    val actionTextField = new TextField()
    //actionTextField.setPrefHeight(ActionHeight)
    actionTextField.setText(actionText)
    actionTextField.setId(id)

    actionPane.getChildren.addAll(actionTypeLabel, actionTextField)

    section.getChildren.add(actionPane)
  }
}
