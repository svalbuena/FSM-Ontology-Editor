package infrastructure.propertybox

import javafx.scene.control.Label
import javafx.scene.layout.VBox

class PropertiesBox extends VBox {
  setStyle("-fx-background-color: #b3c6b3")

  val boxTitle = new Label()
  boxTitle.setText("Properties")

  getChildren.add(boxTitle)
}
