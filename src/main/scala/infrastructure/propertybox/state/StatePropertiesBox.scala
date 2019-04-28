package infrastructure.propertybox.state

import infrastructure.element.action.ActionType.ActionType
import infrastructure.propertybox.action.ActionPropertiesBox
import infrastructure.propertybox.{LabelTextFieldSection, LabelVBoxSection}
import javafx.scene.control.Label
import javafx.scene.layout.VBox

class StatePropertiesBox extends VBox {
  private val stateTitleLabel = new Label()
  stateTitleLabel.setText("State")

  private val nameSection = new LabelTextFieldSection()
  nameSection.setLabelText("Name:")

  private val entryActionsSection = new LabelVBoxSection[ActionPropertiesBox]
  entryActionsSection.setLabelText("Entry actions:")
  entryActionsSection.setButtonText("Add entry action")

  private val exitActionsSection = new LabelVBoxSection[ActionPropertiesBox]
  exitActionsSection.setLabelText("Exit actions:")
  exitActionsSection.setButtonText("Add exit action")

  getChildren.addAll(stateTitleLabel, nameSection, entryActionsSection, exitActionsSection)


  def setOnStateNameChanged(stateNameChangedHandler: String => Unit): Unit = nameSection.setOnTextChanged(stateNameChangedHandler)

  def setName(name: String): Unit = nameSection.setText(name)

  def addAction(actionPropertiesBox: ActionPropertiesBox, actionType: ActionType, title: String): Unit = {
    actionType match {
      case infrastructure.element.action.ActionType.ENTRY => entryActionsSection.addPane(actionPropertiesBox, title)
      case infrastructure.element.action.ActionType.EXIT => exitActionsSection.addPane(actionPropertiesBox, title)
      case _ =>
    }
  }

  def removeAction(actionPropertiesBox: ActionPropertiesBox, actionType: ActionType): Unit = {
    actionType match {
      case infrastructure.element.action.ActionType.ENTRY => entryActionsSection.removePane(actionPropertiesBox)
      case infrastructure.element.action.ActionType.EXIT => exitActionsSection.removePane(actionPropertiesBox)
      case _ =>
    }
  }

  def setOnAddEntryActionButtonClicked(callback: () => Unit): Unit = entryActionsSection.setButtonCallback(callback)

  def setOnAddExitActionButtonClicked(callback: () => Unit): Unit = exitActionsSection.setButtonCallback(callback)
}
