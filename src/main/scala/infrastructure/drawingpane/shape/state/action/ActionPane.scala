package infrastructure.drawingpane.shape.state.action

import infrastructure.elements.action.Action
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
      case infrastructure.elements.action.ActionType.ENTRY => "entry/"
      case infrastructure.elements.action.ActionType.EXIT => "exit/"
    }

    actionTypeLabel.setText(actionTypeText)
  }

  def setActionName(name: String): Unit = actionTextLabel.setText(name)
}
