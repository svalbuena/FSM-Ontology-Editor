package infrastructure.propertybox

import infrastructure.propertybox.fsm.FsmPropertiesBox
import infrastructure.propertybox.state.StatePropertiesBox
import infrastructure.propertybox.transition.TransitionPropertiesBox
import javafx.geometry.Insets
import javafx.scene.Node
import javafx.scene.control.{Label, ScrollPane, TitledPane}
import javafx.scene.layout.{Pane, Region, VBox}

class PropertiesBoxBar extends TitledPane {
  setText("Properties")

  private val fsmPropertiesBoxWrapper = new TitledPane()
  private val otherPropertiesBoxWrapper = new TitledPane()

  private val propertiesPane = new VBox()
  propertiesPane.getChildren.addAll(fsmPropertiesBoxWrapper, otherPropertiesBoxWrapper)

  private val scrollPane = new ScrollPane()
  scrollPane.setContent(propertiesPane)

  setContent(scrollPane)

  removeFsmPropertiesBox()
  removeOtherPropertiesBoxContent()

  setStyle()


  def setFsmPropertiesBox(fsmPropertiesBox: FsmPropertiesBox): Unit = {
    fsmPropertiesBoxWrapper.setText("Finite State Machine")
    fsmPropertiesBoxWrapper.setContent(fsmPropertiesBox)
    fsmPropertiesBoxWrapper.setVisible(true)

    setFsmPropertiesBoxStyle(fsmPropertiesBox)
  }

  def removeFsmPropertiesBox(): Unit = {
    fsmPropertiesBoxWrapper.setContent _
    fsmPropertiesBoxWrapper.setVisible(false)
  }

  def setOtherPropertiesBoxContent(statePropertiesBox: StatePropertiesBox): Unit = setOtherPropertiesBoxContent(statePropertiesBox, "State")

  def setOtherPropertiesBoxContent(transitionPropertiesBox: TransitionPropertiesBox): Unit = setOtherPropertiesBoxContent(transitionPropertiesBox, "Transition")

  def removeOtherPropertiesBoxContent(): Unit = {
    otherPropertiesBoxWrapper.setContent _
    otherPropertiesBoxWrapper.setVisible(false)
  }

  def removeOtherPropertiesBoxContentIfEqual(node: Node): Unit = {
    val content = otherPropertiesBoxWrapper.getContent
    if (content != null && content == node) {
      removeOtherPropertiesBoxContent()
    }
  }

  private def setOtherPropertiesBoxContent(region: Region, title: String): Unit = {
    otherPropertiesBoxWrapper.setText(title)
    otherPropertiesBoxWrapper.setContent(region)
    otherPropertiesBoxWrapper.setVisible(true)

    setContentStyle(region)
  }

  private def setFsmPropertiesBoxStyle(fsmPropertiesBox: FsmPropertiesBox): Unit = {
    fsmPropertiesBox.prefWidthProperty().bind(widthProperty())
    setContentStyle(fsmPropertiesBox)
  }

  private def setContentStyle(region: Region): Unit = {
    //region.setPadding(insets)
  }

  private def setStyle(): Unit = {
    fsmPropertiesBoxWrapper.prefWidthProperty().bind(propertiesPane.widthProperty())
    //fsmPropertiesBoxWrapper.setCollapsible(false)
    fsmPropertiesBoxWrapper.getStyleClass.add("properties-content-titled-pane")

    otherPropertiesBoxWrapper.prefWidthProperty().bind(propertiesPane.widthProperty())
    otherPropertiesBoxWrapper.setCollapsible(false)
    otherPropertiesBoxWrapper.getStyleClass.add("properties-content-titled-pane")

    propertiesPane.setFillWidth(true)
    scrollPane.setFitToWidth(true)

    val insets = new Insets(2)
    scrollPane.setPadding(insets)

    setCollapsible(false)
    setStyle("-fx-background-color: #b3c6b3")
    getStyleClass.add("properties-titled-pane")
  }
}
