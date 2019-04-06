package infrastructure.propertybox.state

import infrastructure.elements.action.Action
import infrastructure.elements.node.State
import infrastructure.propertybox.action.ActionPropertiesBox
import infrastructure.propertybox.state.section.{ActionsSection, NameSection}
import javafx.scene.layout.{HBox, Pane, VBox}

class StatePropertiesBox extends VBox {
  private val nameSection = new NameSection()
  private val actionsSection = new ActionsSection()

  getChildren.addAll(nameSection, actionsSection)


  def setOnStateNameChanged(stateNameChangedHandler: String => Unit): Unit = {
    nameSection.setOnStateNameChanged(stateNameChangedHandler)
  }

  def setName(name: String): Unit = nameSection.setName(name)
  def addEntryAction(actionPropertiesBox: ActionPropertiesBox): Unit = actionsSection.addEntryAction(actionPropertiesBox)
  def addExitAction(actionPropertiesBox: ActionPropertiesBox): Unit = actionsSection.addExitAction(actionPropertiesBox)
}
