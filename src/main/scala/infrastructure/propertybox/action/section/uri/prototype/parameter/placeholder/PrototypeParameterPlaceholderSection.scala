package infrastructure.propertybox.action.section.uri.prototype.parameter.placeholder

import javafx.scene.control.{Label, TextField}
import javafx.scene.layout.HBox

class PrototypeParameterPlaceholderSection extends HBox {
  private val placeholderLabel = new Label()
  placeholderLabel.setText("Placeholder:")

  private val placeholderTextField = new TextField()

  getChildren.addAll(placeholderLabel, placeholderTextField)


  def setPlaceholder(placeholder: String): Unit = placeholderTextField.setText(placeholder)

  def setOnParameterPlaceholderChanged(parameterPlaceholderChangedHandler: String => Unit): Unit = {
    placeholderTextField.setOnKeyTyped(event => {
      parameterPlaceholderChangedHandler(placeholderTextField.getText)
    })
  }
}
