package infrastructure.propertybox

import javafx.geometry.Pos
import javafx.scene.control.{Button, Label}
import javafx.scene.layout.{HBox, Priority, Region}

/**
  * Template for a section with a label and a button
  */
class LabelButtonSection extends HBox {
  private val label = new Label()
  private val spacingRegion = new Region()
  private val button = new Button()

  getChildren.addAll(label, spacingRegion, button)

  setStyle()


  def setLabelText(labelText: String): Unit = label.setText(labelText)

  def setButtonText(buttonText: String): Unit = button.setText(buttonText)

  def setButtonCallback(callback: () => Unit): Unit = button.setOnAction(_ => callback())

  private def setStyle(): Unit = {
    getStyleClass.add("properties-box-hbox")

    setAlignment(Pos.CENTER_LEFT)
    HBox.setHgrow(spacingRegion, Priority.ALWAYS)
    label.getStyleClass.add("properties-h2")
  }
}
