package infrastructure.propertybox

import javafx.scene.control.Label
import javafx.scene.layout.{Pane, VBox}

class PropertiesBox extends VBox {
  setStyle("-fx-background-color: #b3c6b3")

  val boxTitle = new Label()
  boxTitle.setText("Properties")

  val propertiesSection = new Pane

  getChildren.addAll(boxTitle, propertiesSection)

  def setContent(pane: Pane): Unit = {
    propertiesSection.getChildren.removeAll(propertiesSection.getChildren)
    propertiesSection.getChildren.add(pane)
  }
}
