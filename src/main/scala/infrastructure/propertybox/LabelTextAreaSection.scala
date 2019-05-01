package infrastructure.propertybox

import javafx.scene.control.{Label, TextArea}
import javafx.scene.layout.VBox

/**
  * Template for a section with a label and a text area
  */
class LabelTextAreaSection extends VBox {
  private val label = new Label()
  private val textArea = new TextArea()

  getChildren.addAll(label, textArea)

  setStyle()

  def setLabelText(labelText: String): Unit = label.setText(labelText)

  def setText(text: String): Unit = textArea.setText(text)

  def setOnTextChanged(textChangedHandler: String => Unit): Unit = {
    textArea.setOnKeyTyped(_ => {
      textChangedHandler(textArea.getText)
    })
  }

  private def setStyle(): Unit = {
    getStyleClass.add("properties-box-hbox")
  }
}
