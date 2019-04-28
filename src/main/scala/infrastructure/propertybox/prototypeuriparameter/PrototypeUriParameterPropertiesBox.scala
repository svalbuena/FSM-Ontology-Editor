package infrastructure.propertybox.prototypeuriparameter

import infrastructure.propertybox.LabelTextFieldSection
import javafx.scene.control.Button
import javafx.scene.layout.VBox

class PrototypeUriParameterPropertiesBox extends VBox {
  private val removeButton = new Button()
  removeButton.setText("Remove")

  private val placeholderSection = new LabelTextFieldSection
  placeholderSection.setLabelText("Placeholder:")

  private val querySection = new LabelTextFieldSection
  querySection.setLabelText("Query:")

  getChildren.addAll(removeButton, placeholderSection, querySection)

  def setPlaceholder(placeholder: String): Unit = placeholderSection.setText(placeholder)

  def setOnParameterPlaceholderChanged(parameterPlaceholderChangedHandler: String => Unit): Unit = placeholderSection.setOnTextChanged(parameterPlaceholderChangedHandler)

  def setQuery(query: String): Unit = querySection.setText(query)

  def setOnParameterQueryChanged(parameterQueryChangedHandler: String => Unit): Unit = querySection.setOnTextChanged(parameterQueryChangedHandler)

  def setOnRemoveParameterButtonClicked(callback: () => Unit): Unit = removeButton.setOnAction(_ => callback())
}
