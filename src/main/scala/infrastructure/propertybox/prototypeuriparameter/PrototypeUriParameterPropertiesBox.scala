package infrastructure.propertybox.prototypeuriparameter

import infrastructure.propertybox.{LabelButtonSection, LabelTextAreaSection, LabelTextFieldSection}
import javafx.scene.layout.VBox

/**
  * Properties box of a prototype uri parameter
  */
class PrototypeUriParameterPropertiesBox extends VBox {
  private val titleAndRemoveSection = new LabelButtonSection
  titleAndRemoveSection.setLabelText("Parameter")
  titleAndRemoveSection.setButtonText("Remove")

  private val nameSection = new LabelTextFieldSection
  nameSection.setLabelText("Name:")

  private val placeholderSection = new LabelTextFieldSection
  placeholderSection.setLabelText("Placeholder:")

  private val querySection = new LabelTextAreaSection
  querySection.setLabelText("Query:")

  getChildren.addAll(titleAndRemoveSection, nameSection, placeholderSection, querySection)

  setStyle()

  def setName(name: String): Unit = nameSection.setText(name)

  def setOnNameChanged(nameChangedHandler: String => Unit): Unit = nameSection.setOnTextChanged(nameChangedHandler)

  def setPlaceholder(placeholder: String): Unit = placeholderSection.setText(placeholder)

  def setOnParameterPlaceholderChanged(parameterPlaceholderChangedHandler: String => Unit): Unit = placeholderSection.setOnTextChanged(parameterPlaceholderChangedHandler)

  def setQuery(query: String): Unit = querySection.setText(query)

  def setOnParameterQueryChanged(parameterQueryChangedHandler: String => Unit): Unit = querySection.setOnTextChanged(parameterQueryChangedHandler)

  def setOnRemoveParameterButtonClicked(callback: () => Unit): Unit = titleAndRemoveSection.setButtonCallback(callback)

  private def setStyle(): Unit = {
    getStyleClass.add("properties-box-vbox")
  }
}
