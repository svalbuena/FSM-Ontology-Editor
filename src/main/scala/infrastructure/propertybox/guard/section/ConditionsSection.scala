package infrastructure.propertybox.guard.section

import infrastructure.propertybox.condition.ConditionPropertiesBox
import javafx.scene.control.Label
import javafx.scene.layout.VBox

class ConditionsSection extends VBox {
  private val titleLabel = new Label()
  titleLabel.setText("Conditions:")

  private val conditionsPane = new VBox()

  getChildren.addAll(titleLabel, conditionsPane)


  def addCondition(conditionPropertiesBox: ConditionPropertiesBox): Unit = conditionsPane.getChildren.add(conditionPropertiesBox)
  def removeCondition(conditionPropertiesBox: ConditionPropertiesBox): Unit = conditionsPane.getChildren.remove(conditionPropertiesBox)
}
