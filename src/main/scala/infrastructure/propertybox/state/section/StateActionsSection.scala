package infrastructure.propertybox.state.section

import infrastructure.element.action.ActionType.ActionType
import infrastructure.propertybox.action.ActionPropertiesBox
import javafx.scene.control.{Button, Label}
import javafx.scene.layout.{HBox, Pane, VBox}

class StateActionsSection extends VBox {
  private val titleLabel = new Label()
  titleLabel.setText("Actions")

  private val addEntryActionButton = new Button()
  addEntryActionButton.setText("Add entry action")

  private val addExitActionButton = new Button()
  addExitActionButton.setText("Add exit action")

  private val titleAndButtonsPane = new HBox()
  titleAndButtonsPane.getChildren.addAll(titleLabel, addEntryActionButton, addExitActionButton)

  private val entryActionsSection = new VBox()
  private val exitActionsSection = new VBox()

  private val actionsSection = new VBox()
  actionsSection.getChildren.addAll(entryActionsSection, exitActionsSection)

  getChildren.addAll(titleAndButtonsPane, actionsSection)


  def addAction(actionPropertiesBox: ActionPropertiesBox, actionType: ActionType): Unit = {
    val section = getPaneForActionType(actionType)

    addActionToSection(actionPropertiesBox, section)
  }

  def removeAction(actionPropertiesBox: ActionPropertiesBox, actionType: ActionType): Unit = {
    val section = getPaneForActionType(actionType)

    removeActionFromSection(actionPropertiesBox, section)
  }

  def setOnAddEntryActionButtonClicked(callback: () => Unit): Unit = {
    addEntryActionButton.setOnMouseClicked(event => {
      callback()
    })
  }

  def setOnAddExitActionButtonClicked(callback: () => Unit): Unit = {
    addExitActionButton.setOnMouseClicked(event => {
      callback()
    })
  }


  private def getPaneForActionType(actionType: ActionType): Pane = {
    actionType match {
      case infrastructure.element.action.ActionType.ENTRY => entryActionsSection
      case infrastructure.element.action.ActionType.EXIT => exitActionsSection
    }
  }

  private def addActionToSection(actionPropertiesBox: ActionPropertiesBox, section: Pane): Unit = section.getChildren.add(actionPropertiesBox)

  private def removeActionFromSection(actionPropertiesBox: ActionPropertiesBox, section: Pane): Unit = section.getChildren.remove(actionPropertiesBox)

}
