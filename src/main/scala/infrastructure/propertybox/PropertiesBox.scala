package infrastructure.propertybox

import javafx.scene.Group
import javafx.scene.control.{Label, ScrollPane}
import javafx.scene.layout.{Pane, VBox}

class PropertiesBox extends VBox {
  setStyle("-fx-background-color: #b3c6b3")

  val boxTitle = new Label()
  boxTitle.setText("Properties")

  val propertiesSection = new ScrollPane()
  removeContent()

  getChildren.addAll(boxTitle, propertiesSection)

  def setContent(pane: Pane): Unit = {
    propertiesSection.setContent(pane)
    propertiesSection.setVisible(true)
  }

  def removeContent(): Unit = {
    propertiesSection.setContent _
    propertiesSection.setVisible(false)
  }
}
