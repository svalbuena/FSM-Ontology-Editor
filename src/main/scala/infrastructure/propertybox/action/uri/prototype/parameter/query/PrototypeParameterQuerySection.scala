package infrastructure.propertybox.action.uri.prototype.parameter.query

import javafx.scene.control.{Label, TextField}
import javafx.scene.layout.HBox

class PrototypeParameterQuerySection extends HBox {
  private val queryLabel = new Label()
  queryLabel.setText("Query:")

  private val queryTextField = new TextField()

  getChildren.addAll(queryLabel, queryTextField)

  def setQuery(query: String): Unit = queryTextField.setText(query)

  def setOnParameterQueryChanged(parameterQueryChangedHandler: String => Unit): Unit = {
    queryTextField.setOnKeyTyped(event => {
      parameterQueryChangedHandler(queryTextField.getText)
    })
  }
}
