package infrastructure.propertybox.action.nametype

import infrastructure.elements.action.ActionType.{ActionType, ENTRY, EXIT}
import javafx.scene.control.{Label, TextField}
import javafx.scene.layout.HBox

class NameTypeSection() extends HBox {
  private val actionTypeLabel = new Label()
  private val actionNameTextField = new TextField()

  getChildren.addAll(actionTypeLabel, actionNameTextField)


  def setActionType(actionType: ActionType): Unit = {
    val typeText = actionType match {
      case ENTRY => "/entry"
      case EXIT => "/exit"
    }
    actionTypeLabel.setText(typeText)
  }

  def setActionName(name: String): Unit = actionNameTextField.setText(name)

  def setOnActionNameChanged(actionNameChangedHandler: String => Unit): Unit = {
    actionNameTextField.setOnKeyTyped(event => {
      actionNameChangedHandler(actionNameTextField.getText)
    })
  }
}
