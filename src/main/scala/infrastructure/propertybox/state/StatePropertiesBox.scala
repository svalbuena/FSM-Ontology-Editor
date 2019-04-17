package infrastructure.propertybox.state

import infrastructure.element.action.ActionType.ActionType
import infrastructure.propertybox.action.ActionPropertiesBox
import infrastructure.propertybox.state.section.{StateActionsSection, StateNameSection}
import javafx.scene.control.Label
import javafx.scene.layout.VBox

class StatePropertiesBox extends VBox {
  private val stateTitleLabel = new Label()
  stateTitleLabel.setText("State")
  private val nameSection = new StateNameSection()
  private val actionsSection = new StateActionsSection()

  getChildren.addAll(stateTitleLabel, nameSection, actionsSection)


  def setOnStateNameChanged(stateNameChangedHandler: String => Unit): Unit = nameSection.setOnStateNameChanged(stateNameChangedHandler)

  def setName(name: String): Unit = nameSection.setName(name)

  def addAction(actionPropertiesBox: ActionPropertiesBox, actionType: ActionType): Unit = actionsSection.addAction(actionPropertiesBox, actionType)

  def removeAction(actionPropertiesBox: ActionPropertiesBox, actionType: ActionType): Unit = actionsSection.removeAction(actionPropertiesBox, actionType)

  def setOnAddEntryActionButtonClicked(callback: () => Unit): Unit = actionsSection.setOnAddEntryActionButtonClicked(callback)

  def setOnAddExitActionButtonClicked(callback: () => Unit): Unit = actionsSection.setOnAddExitActionButtonClicked(callback)
}
