package infrastructure.drawingpane.shape.state.section


import infrastructure.elements.action.{EntryAction, ExitAction}
import javafx.collections.ObservableList
import javafx.geometry.Insets
import javafx.scene.control.{Label, TextField}
import javafx.scene.layout.{HBox, Pane, VBox}

class ActionsSection(val ActionHeight: Double) extends VBox {
  private val Padding = 10.0

  private val entryActionsSection = new VBox()
  private val exitActionsSection = new VBox()

  getStyleClass.add("actions-area")
  setPadding(new Insets(Padding))

  getChildren.addAll(entryActionsSection, exitActionsSection)

  def setEntryActions(entryActions: List[EntryAction]): Unit = {
    entryActionsSection.getChildren.removeAll(entryActionsSection.getChildren)
    entryActions.foreach(entryAction => addActionToSection("entry/", entryAction.text, entryActionsSection))
  }

  def setExitActions(exitActions: List[ExitAction]): Unit = {
    exitActionsSection.getChildren.removeAll(exitActionsSection.getChildren)
    exitActions.foreach(exitAction => addActionToSection("exit/", exitAction.text, exitActionsSection))
  }

  private def addActionToSection(actionType: String, actionText: String, section: Pane): Unit = {
    val actionPane = new HBox()

    val actionLabel = new Label()
    actionLabel.setText(actionType + actionText)
    actionLabel.setPrefHeight(ActionHeight)

    actionPane.getChildren.add(actionLabel)

    section.getChildren.add(actionPane)
  }
}
