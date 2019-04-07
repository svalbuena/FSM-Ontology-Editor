package infrastructure.propertybox.action.nametype

import infrastructure.elements.action.ActionType.{ActionType, ENTRY, EXIT, GUARD}
import javafx.scene.control.{Button, Label, TextField}
import javafx.scene.layout.HBox

class NameTypeSection() extends HBox {
  private val actionTypeLabel = new Label()
  private val actionNameTextField = new TextField()
  private val removeButton = new Button()
  removeButton.setText("Remove")

  getChildren.addAll(actionTypeLabel, actionNameTextField, removeButton)


  def setActionType(actionType: ActionType): Unit = {
    val typeText = actionType match {
      case ENTRY => "/entry"
      case EXIT => "/exit"
      case GUARD => "/"
    }
    actionTypeLabel.setText(typeText)
  }

  def setActionName(name: String): Unit = actionNameTextField.setText(name)

  def setOnActionNameChanged(actionNameChangedHandler: String => Unit): Unit = {
    actionNameTextField.setOnKeyTyped(event => {
      actionNameChangedHandler(actionNameTextField.getText)
    })
  }

  def setOnRemoveActionButtonClicked(callback: () => Unit): Unit = {
    removeButton.setOnMouseClicked(event => {
      callback()
    })
  }
}
