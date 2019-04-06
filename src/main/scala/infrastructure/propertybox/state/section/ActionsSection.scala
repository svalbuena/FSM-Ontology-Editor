package infrastructure.propertybox.state.section

import infrastructure.elements.action.Action
import infrastructure.propertybox.action.ActionPropertiesBox
import javafx.scene.control.ScrollPane.ScrollBarPolicy
import javafx.scene.control.{Label, ScrollPane, TextField}
import javafx.scene.layout.{HBox, Pane, VBox}

class ActionsSection extends VBox {
  private val titleLabel = new Label()
  titleLabel.setText("Actions")

  private val entryActionsSection = new VBox()
  private val exitActionsSection = new VBox()

  private val actionsSection = new VBox()
  actionsSection.getChildren.addAll(entryActionsSection, exitActionsSection)

  getChildren.addAll(titleLabel, actionsSection)


  def addEntryAction(actionPropertiesBox: ActionPropertiesBox): Unit = {
    addActionToSection(actionPropertiesBox, entryActionsSection)
  }

  def addExitAction(actionPropertiesBox: ActionPropertiesBox): Unit = {
    addActionToSection(actionPropertiesBox, exitActionsSection)
  }

  private def addActionToSection(actionPropertiesBox: ActionPropertiesBox, section: Pane): Unit = section.getChildren.add(actionPropertiesBox)

}
