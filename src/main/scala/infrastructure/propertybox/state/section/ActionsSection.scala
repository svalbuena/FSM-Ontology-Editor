package infrastructure.propertybox.state.section

import infrastructure.drawingpane.shape.state.action.ActionPane
import infrastructure.elements.action.Action
import infrastructure.elements.action.ActionType.ActionType
import infrastructure.propertybox.action.ActionPropertiesBox
import javafx.scene.control.ScrollPane.ScrollBarPolicy
import javafx.scene.control.{Button, Label, ScrollPane, TextField}
import javafx.scene.layout.{HBox, Pane, VBox}

class ActionsSection extends VBox {
  private val titleAndButtonsPane = new HBox()

  private val titleLabel = new Label()
  titleLabel.setText("Actions")

  private val addEntryActionButton = new Button()
  addEntryActionButton.setText("Add entry action")

  private val addExitActionButton = new Button()
  addExitActionButton.setText("Add exit action")

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
      case infrastructure.elements.action.ActionType.ENTRY => entryActionsSection
      case infrastructure.elements.action.ActionType.EXIT => exitActionsSection
    }
  }

  private def addActionToSection(actionPropertiesBox: ActionPropertiesBox, section: Pane): Unit = section.getChildren.add(actionPropertiesBox)
  private def removeActionFromSection(actionPropertiesBox: ActionPropertiesBox, section: Pane): Unit = section.getChildren.remove(actionPropertiesBox)

}
