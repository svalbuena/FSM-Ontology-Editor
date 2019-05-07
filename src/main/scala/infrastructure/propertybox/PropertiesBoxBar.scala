package infrastructure.propertybox

import infrastructure.propertybox.fsm.FsmPropertiesBox
import infrastructure.propertybox.state.StatePropertiesBox
import infrastructure.propertybox.transition.TransitionPropertiesBox
import javafx.scene.Node
import javafx.scene.control.{ScrollPane, TitledPane}
import javafx.scene.layout.{Region, VBox}

/**
  * PropertiesBoxBar that appears on the application
  */
class PropertiesBoxBar extends ScrollPane {
  private val content = new TitledPane()
  content.setText("Properties")

  private val subContent = new VBox()

  private val fsmPropertiesBoxWrapper = new TitledPane()
  private val otherPropertiesBoxWrapper = new TitledPane()

  subContent.getChildren.addAll(fsmPropertiesBoxWrapper, otherPropertiesBoxWrapper)

  content.setContent(subContent)

  setContent(content)

  removeFsmPropertiesBox()
  removeOtherPropertiesBoxContent()

  setStyle()


  def setFsmPropertiesBox(fsmPropertiesBox: FsmPropertiesBox): Unit = {
    fsmPropertiesBoxWrapper.setText("Finite State Machine")
    fsmPropertiesBoxWrapper.setContent(fsmPropertiesBox)
    fsmPropertiesBoxWrapper.setVisible(true)

    setFsmPropertiesBoxStyle(fsmPropertiesBox)
  }

  private def setFsmPropertiesBoxStyle(fsmPropertiesBox: FsmPropertiesBox): Unit = {
    fsmPropertiesBox.prefWidthProperty().bind(widthProperty())
    setContentStyle(fsmPropertiesBox)
  }

  def removeFsmPropertiesBox(): Unit = {
    fsmPropertiesBoxWrapper.setContent _
    fsmPropertiesBoxWrapper.setVisible(false)
  }

  def setOtherPropertiesBoxContent(statePropertiesBox: StatePropertiesBox): Unit = setOtherPropertiesBoxContent(statePropertiesBox, "State")

  def setOtherPropertiesBoxContent(transitionPropertiesBox: TransitionPropertiesBox): Unit = setOtherPropertiesBoxContent(transitionPropertiesBox, "Transition")

  private def setOtherPropertiesBoxContent(region: Region, title: String): Unit = {
    otherPropertiesBoxWrapper.setText(title)
    otherPropertiesBoxWrapper.setContent(region)
    otherPropertiesBoxWrapper.setVisible(true)

    setContentStyle(region)
  }

  private def setContentStyle(region: Region): Unit = {
    //region.setPadding(insets)
  }

  def removeOtherPropertiesBoxContentIfEqual(node: Node): Unit = {
    val content = otherPropertiesBoxWrapper.getContent
    if (content != null && content == node) {
      removeOtherPropertiesBoxContent()
    }
  }

  def removeOtherPropertiesBoxContent(): Unit = {
    otherPropertiesBoxWrapper.setContent _
    otherPropertiesBoxWrapper.setVisible(false)
  }

  private def setStyle(): Unit = {
    content.prefHeightProperty().bind(heightProperty())

    fsmPropertiesBoxWrapper.prefWidthProperty().bind(subContent.widthProperty())
    //fsmPropertiesBoxWrapper.setCollapsible(false)
    fsmPropertiesBoxWrapper.getStyleClass.add("properties-content-titled-pane")

    otherPropertiesBoxWrapper.prefWidthProperty().bind(subContent.widthProperty())
    otherPropertiesBoxWrapper.setCollapsible(false)
    otherPropertiesBoxWrapper.getStyleClass.add("properties-content-titled-pane")

    content.getStyleClass.add("properties-titled-pane")

    subContent.getStyleClass.add("properties-subcontent")

    getStyleClass.add("properties-scrollpane")
    setFitToWidth(true)

    content.setCollapsible(false)
  }
}
