package infrastructure.drawingpane.shape.state.section


import infrastructure.elements.action.{EntryAction, ExitAction}
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
    actionTextField.setPrefHeight(ActionHeight)
    actionTextField.setText(actionText)

    actionPane.getChildren.addAll(actionTypeLabel, actionTextField)

    section.getChildren.add(actionPane)
  }
}
