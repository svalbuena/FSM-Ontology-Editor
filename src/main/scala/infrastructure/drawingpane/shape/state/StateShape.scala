package infrastructure.drawingpane.shape.state

import infrastructure.drawingpane.shape.ConnectableShape
import infrastructure.drawingpane.shape.state.action.{ActionPane, ActionsSection}
import infrastructure.drawingpane.shape.state.name.NameSection
import infrastructure.elements.action.Action
import infrastructure.elements.action.ActionType.ActionType
import infrastructure.elements.node.State
import javafx.scene.layout.{Pane, StackPane, VBox}

// TODO Move shape and text area
class StateShape extends ConnectableShape {
  private val ActionHeight = 25.0
  private val Width = 250.0
  private val Height = ActionHeight * 4

  private val TitleAreaHeightRatio = 0.20
  private val ActionsAreaHeightRatio = 1 - TitleAreaHeightRatio

  private val pane = new VBox

  pane.setPrefSize(Width, Height)
  pane.getStyleClass.add("state")

  private val nameSection = new NameSection()
  nameSection.prefHeightProperty.bind(pane.heightProperty.multiply(TitleAreaHeightRatio))

  private val actionsSection = new ActionsSection()
  actionsSection.prefHeightProperty.bind(pane.heightProperty.multiply(ActionsAreaHeightRatio))

  pane.getChildren.addAll(nameSection, actionsSection)

  getChildren.add(pane)


  def setName(name: String): Unit = nameSection.setName(name)
  def addAction(actionPane: ActionPane, actionType: ActionType): Unit = actionsSection.addAction(actionPane, actionType)
  def removeAction(actionPane: ActionPane, actionType: ActionType): Unit = actionsSection.removeAction(actionPane, actionType)
}
