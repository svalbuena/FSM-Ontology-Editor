package infrastructure.propertybox

import javafx.geometry.Pos
import javafx.scene.control.{Label, TextField}
import javafx.scene.layout.{HBox, Region}

class LabelTextFieldSection extends HBox {
  private val label = new Label()
  private val textField = new TextField()

  getChildren.addAll(label, textField)

  setStyle()


  def setLabelText(labelText: String): Unit = label.setText(labelText)

  def setText(text: String): Unit = textField.setText(text)

  def setOnTextChanged(textChangedHandler: String => Unit): Unit = {
    textField.setOnKeyTyped(_ => {
      textChangedHandler(textField.getText)
    })
  }

  private def setStyle(): Unit = {
    setAlignment(Pos.CENTER_LEFT)

    label.setMinWidth(Region.USE_PREF_SIZE)
    textField.prefWidthProperty().bind(widthProperty().subtract(label.widthProperty()))
  }
}
