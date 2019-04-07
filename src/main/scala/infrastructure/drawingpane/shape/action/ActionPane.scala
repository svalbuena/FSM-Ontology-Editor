package infrastructure.drawingpane.shape.action

import infrastructure.elements.action.ActionType
import infrastructure.elements.action.ActionType.ActionType
import javafx.scene.control.Label
import javafx.scene.layout.HBox

class ActionPane extends HBox {
  private val ActionHeight = 10.0

  private val actionTypeLabel = new Label()
  actionTypeLabel.setPrefHeight(ActionHeight)

  private val actionTextLabel = new Label()
  actionTextLabel.setPrefHeight(ActionHeight)

  getChildren.addAll(actionTypeLabel, actionTextLabel)

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
