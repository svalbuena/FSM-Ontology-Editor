package infrastructure.propertybox.action.section

import javafx.scene.control.{Label, TextField}
import javafx.scene.layout.HBox

class AbsoluteUriSection(absoluteUri: String) extends HBox {
  private val absoluteUriLabel = new Label()
  absoluteUriLabel.setText("Absolute URI:")

  private val absoluteUriTextField = new TextField()
  absoluteUriTextField.setText(absoluteUri)

  getChildren.addAll(absoluteUriLabel, absoluteUriTextField)
}
