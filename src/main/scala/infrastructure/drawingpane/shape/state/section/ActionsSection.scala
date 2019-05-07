package infrastructure.drawingpane.shape.state.section

import infrastructure.drawingpane.shape.action.ActionPane
import infrastructure.element.action.ActionType.ActionType
import javafx.geometry.Insets
import javafx.scene.layout.{Pane, VBox}

/**
  * Section of guards of a state
  */
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

  private def addActionToSection(actionPane: ActionPane, section: Pane): Unit = section.getChildren.add(actionPane)

  def removeAction(actionPane: ActionPane, actionType: ActionType): Unit = {
    val section = getPaneForActionType(actionType)

    removeActionFromSection(actionPane, section)
  }

  private def getPaneForActionType(actionType: ActionType): Pane = {
    actionType match {
      case infrastructure.element.action.ActionType.ENTRY => entryActionsSection
      case infrastructure.element.action.ActionType.EXIT => exitActionsSection
    }
  }

  private def removeActionFromSection(actionPane: ActionPane, section: Pane): Unit = section.getChildren.remove(actionPane)

}
