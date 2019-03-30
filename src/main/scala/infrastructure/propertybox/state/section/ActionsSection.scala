package infrastructure.propertybox.state.section

import infrastructure.elements.action.{EntryAction, ExitAction}
import javafx.scene.control.{Label, ScrollPane, TextField}
import javafx.scene.layout.{HBox, Pane, VBox}

class ActionsSection(entryActions: List[EntryAction], exitActions: List[ExitAction]) extends VBox {
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


  def setEntryActions(entryActions: List[EntryAction]): Unit = {
    entryActions.foreach(entryAction => addActionToSection("entry/", entryAction.text, entryActionsSection))
  }

  def setExitActions(exitActions: List[ExitAction]): Unit = {
    exitActions.foreach(exitAction => addActionToSection("entry/", exitAction.text, exitActionsSection))
  }

  private def addActionToSection(actionType: String, actionText: String, section: Pane): Unit = {
    val actionPane = new HBox()

    val actionTypeLabel = new Label()
    actionTypeLabel.setText(actionType)

    val actionTextField = new TextField()
    //actionTextField.setPrefHeight(ActionHeight)
    actionTextField.setText(actionText)

    actionPane.getChildren.addAll(actionTypeLabel, actionTextField)

    section.getChildren.add(actionPane)
  }
}
