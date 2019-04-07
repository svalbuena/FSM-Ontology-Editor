package infrastructure.propertybox.guard.section

import infrastructure.propertybox.action.ActionPropertiesBox
import javafx.scene.control.Label
import javafx.scene.layout.VBox

class ActionsSection extends VBox {
  private val titleLabel = new Label()
  titleLabel.setText("Actions:")

  private val actionsPane = new VBox()

  getChildren.addAll(titleLabel, actionsPane)


  def addAction(actionPropertiesBox: ActionPropertiesBox): Unit = actionsPane.getChildren.add(actionPropertiesBox)

  def removeAction(actionPropertiesBox: ActionPropertiesBox): Unit = actionsPane.getChildren.remove(actionPropertiesBox)
}
