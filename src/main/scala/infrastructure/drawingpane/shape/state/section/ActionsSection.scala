package infrastructure.drawingpane.shape.state.section

import infrastructure.drawingpane.shape.action.ActionPane
import infrastructure.elements.action.ActionType.ActionType
import javafx.geometry.Insets
import javafx.scene.layout.{Pane, VBox}

class ActionsSection extends VBox {
  private val Padding = 10.0

  private val entryActionsSection = new VBox()
  private val exitActionsSection = new VBox()

  getStyleClass.add("actions-area")
  setPadding(new Insets(Padding))

  getChildren.addAll(entryActionsSection, exitActionsSection)

  def addAction(actionPane: ActionPane, actionType: ActionType): Unit = {
    val section = getPaneForActionType(actionType)

    addActionToSection(actionPane, section)
  }

  def removeAction(actionPane: ActionPane, actionType: ActionType): Unit = {
    val section = getPaneForActionType(actionType)

    removeActionFromSection(actionPane, section)
  }

  private def getPaneForActionType(actionType: ActionType): Pane = {
    actionType match {
      case infrastructure.elements.action.ActionType.ENTRY => entryActionsSection
      case infrastructure.elements.action.ActionType.EXIT => exitActionsSection
    }
  }

  private def addActionToSection(actionPane: ActionPane, section: Pane): Unit = section.getChildren.add(actionPane)
  private def removeActionFromSection(actionPane: ActionPane, section: Pane): Unit = section.getChildren.remove(actionPane)

}
