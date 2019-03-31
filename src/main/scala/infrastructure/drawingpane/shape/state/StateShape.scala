package infrastructure.drawingpane.shape.state

import infrastructure.drawingpane.shape.ConnectableShape
import infrastructure.drawingpane.shape.state.section.{ActionsSection, NameSection}
import infrastructure.elements.action.{EntryAction, ExitAction}
import infrastructure.elements.node.State
import javafx.scene.layout.{Pane, StackPane, VBox}

// TODO Move shape and text area
class StateShape(val state: State) extends ConnectableShape {
  private val ActionHeight = 25.0
  private val Width = 250.0
  private val Height = ActionHeight * 4

  private val TitleAreaHeightRatio = 0.20
  private val ActionsAreaHeightRatio = 1 - TitleAreaHeightRatio

  private val pane = new VBox

  pane.setPrefSize(Width, Height)
  pane.getStyleClass.add("state")

  private val nameSection = new NameSection(state.name)
  nameSection.prefHeightProperty.bind(pane.heightProperty.multiply(TitleAreaHeightRatio))

  private val actionsSection = new ActionsSection(ActionHeight)
  actionsSection.prefHeightProperty.bind(pane.heightProperty.multiply(ActionsAreaHeightRatio))

  pane.getChildren.addAll(nameSection, actionsSection)

  getChildren.add(pane)

  updateContent()

  def updateContent(): Unit = {
    setName(state.name)
    setEntryActions(state.entryActions)
    setExitActions(state.exitActions)
  }

  def setName(name: String): Unit = nameSection.setName(name)
  def setEntryActions(entryActions: List[EntryAction]): Unit = actionsSection.setEntryActions(entryActions)
  def setExitActions(exitActions: List[ExitAction]): Unit = actionsSection.setExitActions(exitActions)
}
