package infrastructure.drawingpane.shape.state.action

import infrastructure.elements.action.Action
import infrastructure.elements.action.ActionType.ActionType
import javafx.geometry.Insets
import javafx.scene.control.Label
import javafx.scene.layout.{HBox, Pane, VBox}

class ActionsSection extends VBox {
  private val Padding = 10.0

  private val entryActionsSection = new VBox()
  private val exitActionsSection = new VBox()

  getStyleClass.add("actions-area")
  setPadding(new Insets(Padding))

  getChildren.addAll(entryActionsSection, exitActionsSection)

  def addEntryAction(actionPane: ActionPane): Unit = addActionToSection(actionPane, entryActionsSection)
  def addExitAction(actionPane: ActionPane): Unit = addActionToSection(actionPane, exitActionsSection)

  private def addActionToSection(actionPane: ActionPane, section: Pane): Unit = section.getChildren.add(actionPane)
}
