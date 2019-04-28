package infrastructure.propertybox

import infrastructure.propertybox.fsm.FsmPropertiesBox
import javafx.geometry.Insets
import javafx.scene.control.{Label, ScrollPane}
import javafx.scene.layout.{Pane, VBox}

class PropertiesBox extends VBox {
  setStyle("-fx-background-color: #b3c6b3")

  private val boxTitle = new Label()
  boxTitle.setText("Properties")

  private val fsmPropertiesBoxWrapper = new Pane()

  private val propertiesSection = new ScrollPane()
  removeContent()

  getChildren.addAll(boxTitle, fsmPropertiesBoxWrapper, propertiesSection)


  def setFsmPropertiesBox(fsmPropertiesBox: FsmPropertiesBox): Unit = {
    fsmPropertiesBoxWrapper.getChildren.clear()

    fsmPropertiesBoxWrapper.getChildren.add(fsmPropertiesBox)
    fsmPropertiesBox.prefWidthProperty().bind(widthProperty())

    val insets = new Insets(5)
    fsmPropertiesBox.setPadding(insets)
  }

  def removeFsmPropertiesBox(): Unit = {
    fsmPropertiesBoxWrapper.getChildren.clear()
  }

  def setContent(pane: Pane): Unit = {
    propertiesSection.setContent(pane)
    propertiesSection.setVisible(true)
  }

  def removeContent(): Unit = {
    propertiesSection.setContent _
    propertiesSection.setVisible(false)
  }

  def removeContentIfEqual(pane: Pane): Unit = {
    val content = propertiesSection.getContent
    if (content != null && content == pane) {
      removeContent()
    }
  }
}
