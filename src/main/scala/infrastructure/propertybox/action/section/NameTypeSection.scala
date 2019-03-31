package infrastructure.propertybox.action.section

import infrastructure.elements.action.ActionType.{ActionType, ENTRY, EXIT}
import javafx.scene.control.{Label, TextField}
import javafx.scene.layout.HBox

class NameTypeSection(actionType: ActionType, name: String) extends HBox {
  private val actionTypeLabel = new Label()
  private val typeText = actionType match {
    case ENTRY => "/entry"
    case EXIT => "/exit"
  }
  actionTypeLabel.setText(typeText)

  private val actionNameTextField = new TextField()
  actionNameTextField.setText(name)

  getChildren.addAll(actionTypeLabel, actionNameTextField)
}
