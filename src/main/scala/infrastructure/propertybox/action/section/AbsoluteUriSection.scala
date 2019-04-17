package infrastructure.propertybox.action.section

import javafx.scene.control.{Label, TextField}
import javafx.scene.layout.HBox

class AbsoluteUriSection extends HBox {
  private val absoluteUriLabel = new Label()
  absoluteUriLabel.setText("Absolute URI:")

  private val absoluteUriTextField = new TextField()

  getChildren.addAll(absoluteUriLabel, absoluteUriTextField)


  def setAbsoluteUri(absoluteUri: String): Unit = absoluteUriTextField.setText(absoluteUri)

  def setOnAbsoluteUriChanged(absoluteUriChangedHandler: String => Unit): Unit = {
    absoluteUriTextField.setOnKeyTyped(event => {
      absoluteUriChangedHandler(absoluteUriTextField.getText)
    })
  }
}
