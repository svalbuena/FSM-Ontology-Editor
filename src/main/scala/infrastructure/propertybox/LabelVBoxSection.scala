package infrastructure.propertybox

import javafx.scene.control.TitledPane
import javafx.scene.layout.{Pane, VBox}

import scala.collection.mutable

class LabelVBoxSection[T <: Pane](titledPaneStyleClass: String) extends VBox {
  private val titledPaneList: mutable.ListBuffer[TitledPane] = mutable.ListBuffer()

  private val labelButtonSection = new LabelButtonSection
  private val vBox = new VBox()

  getChildren.addAll(labelButtonSection, vBox)

  setStyle()


  def setLabelText(labelText: String): Unit = labelButtonSection.setLabelText(labelText)

  def setButtonText(buttonText: String): Unit = labelButtonSection.setButtonText(buttonText)

  def setButtonCallback(callback: () => Unit): Unit = labelButtonSection.setButtonCallback(callback)

  def addPane(pane: T, title: String): Unit = {
    val titledPane = new TitledPane()
    titledPane.setText(title)
    titledPane.setContent(pane)
    titledPane.setExpanded(false)
    titledPane.getStyleClass.add(titledPaneStyleClass)

    titledPaneList += titledPane
    vBox.getChildren.add(titledPane)
  }

  def removePane(pane: T): Unit = {
    val index = titledPaneList.map(_.getContent).indexOf(pane)
    if (index != -1) {
      val titledPane = titledPaneList(index)

      titledPaneList -= titledPane
      vBox.getChildren.remove(titledPane)
    }
  }

  def setPaneTitle(pane: T, title: String): Unit = {
    val index = titledPaneList.map(_.getContent).indexOf(pane)
    if (index != -1) {
      val titledPane = titledPaneList(index)

      titledPane.setText(title)
    }
  }

  private def setStyle(): Unit = {
    getStyleClass.add("properties-box-hbox")

    /*val vBoxInsets = new Insets(2)
    vBox.setPadding(vBoxInsets)*/
  }
}
