package infrastructure.propertybox

import infrastructure.propertybox.fsm.FsmPropertiesBox
import javafx.scene.control.{Label, ScrollPane}
import javafx.scene.layout.{Pane, VBox}

class PropertiesBox extends VBox {
  setStyle("-fx-background-color: #b3c6b3")

  val boxTitle = new Label()
  boxTitle.setText("Properties")

  val fsmPropertiesBoxWrapper = new Pane()

  val propertiesSection = new ScrollPane()
  removeContent()

  getChildren.addAll(boxTitle, fsmPropertiesBoxWrapper, propertiesSection)


  def setFsmPropertiesBox(fsmPropertiesBox: FsmPropertiesBox): Unit = {
    fsmPropertiesBoxWrapper.getChildren.add(fsmPropertiesBox)
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
