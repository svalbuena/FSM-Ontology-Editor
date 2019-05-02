package infrastructure.drawingpane.shape.condition

import javafx.scene.control.Label
import javafx.scene.layout.Pane

/**
  * Visual representation of a condition
  */
class ConditionPane extends Pane {
  private val conditionNameLabel = new Label()

  getChildren.add(conditionNameLabel)


  /**
    * Sets the name on the condition
    * @param conditionName name of the condition
    */
  def setConditionName(conditionName: String): Unit = conditionNameLabel.setText("[" + conditionName + "]")
}
