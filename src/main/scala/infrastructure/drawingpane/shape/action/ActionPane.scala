package infrastructure.drawingpane.shape.action

import infrastructure.element.action.ActionType
import infrastructure.element.action.ActionType.ActionType
import javafx.scene.control.Label
import javafx.scene.layout.HBox

/**
  * Pane of an action visual element
  */
class ActionPane extends HBox {
  private val ActionHeight = 10.0

  private val actionTypeLabel = new Label()
  actionTypeLabel.setPrefHeight(ActionHeight)

  private val actionTextLabel = new Label()
  actionTextLabel.setPrefHeight(ActionHeight)

  getChildren.addAll(actionTypeLabel, actionTextLabel)

  /**
    * Sets the action type of the visual element
    * @param actionType type of the action
    */
  def setActionType(actionType: ActionType): Unit = {
    val actionTypeText = actionType match {
      case ActionType.ENTRY => "entry/"
      case ActionType.EXIT => "exit/"
      case ActionType.GUARD => " / "
    }

    actionTypeLabel.setText(actionTypeText)
  }

  def setActionName(name: String): Unit = actionTextLabel.setText(name)
}
