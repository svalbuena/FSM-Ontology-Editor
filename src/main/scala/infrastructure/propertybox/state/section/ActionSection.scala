package infrastructure.propertybox.state.section

import infrastructure.elements.action.Action
import infrastructure.elements.action.ActionType.ActionType
import infrastructure.propertybox.action.ActionPropertiesBox
import javafx.scene.control.{Label, TextField, TitledPane}
import javafx.scene.layout.VBox

class ActionSection(action: Action) extends TitledPane {
  val actionPropertiesBox = new ActionPropertiesBox(action)

  setText(action.name)
  setContent(actionPropertiesBox)
}
