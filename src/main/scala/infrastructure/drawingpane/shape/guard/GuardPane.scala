package infrastructure.drawingpane.shape.guard

import infrastructure.drawingpane.shape.action.ActionPane
import infrastructure.drawingpane.shape.condition.ConditionPane
import javafx.scene.control.Label
import javafx.scene.layout.{HBox, VBox}

/**
  * Guard pane
  */
class GuardPane extends VBox {
  private val guardNameLabel = new Label()

  private val conditionsPane = new VBox()
  private val actionsPane = new VBox()
  private val conditionsAndActionsPane = new HBox()
  conditionsAndActionsPane.getChildren.addAll(conditionsPane, actionsPane)

  getChildren.addAll(guardNameLabel, conditionsAndActionsPane)


  def setGuardName(guardName: String): Unit = guardNameLabel.setText(guardName)

  def addCondition(conditionPane: ConditionPane): Unit = conditionsPane.getChildren.add(conditionPane)

  def removeCondition(conditionPane: ConditionPane): Unit = conditionsPane.getChildren.remove(conditionPane)

  def addAction(actionPane: ActionPane): Unit = actionsPane.getChildren.add(actionPane)

  def removeAction(actionPane: ActionPane): Unit = actionsPane.getChildren.remove(actionPane)
}
