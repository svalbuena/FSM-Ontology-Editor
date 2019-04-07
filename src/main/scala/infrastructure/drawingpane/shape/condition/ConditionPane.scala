package infrastructure.drawingpane.shape.condition

import javafx.scene.control.Label
import javafx.scene.layout.Pane

class ConditionPane extends Pane {
  private val conditionNameLabel = new Label()

  getChildren.add(conditionNameLabel)


  def setConditionName(conditionName: String): Unit = conditionNameLabel.setText("[" + conditionName + "]")
}
