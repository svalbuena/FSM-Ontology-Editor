package infrastructure.propertybox.action.section

import javafx.scene.control.{Label, TextField}
import javafx.scene.layout.{HBox, VBox}

class PrototypeParameterSection(query: String, placeholder: String) extends VBox {
  private val querySection = new HBox()

  private val queryLabel = new Label()
  queryLabel.setText("Query:")

  private val queryTextField = new TextField()
  queryTextField.setText(query)

  querySection.getChildren.addAll(queryLabel, queryTextField)


  private val placeholderSection = new HBox()

  private val placeholderLabel = new Label()
  placeholderLabel.setText("Placeholder:")

  private val placeholderTextField = new TextField()
  placeholderTextField.setText(placeholder)

  placeholderSection.getChildren.addAll(placeholderLabel, placeholderTextField)


  getChildren.addAll(querySection, placeholderSection)
}
