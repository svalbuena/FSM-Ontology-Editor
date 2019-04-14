package infrastructure.propertybox.action.section.uri.prototype.parameter

import infrastructure.propertybox.action.section.uri.prototype.parameter.placeholder.PrototypeParameterPlaceholderSection
import infrastructure.propertybox.action.section.uri.prototype.parameter.query.PrototypeParameterQuerySection
import javafx.scene.control.Button
import javafx.scene.layout.VBox

class PrototypeUriParameterPropertiesBox extends VBox {
  private val querySection = new PrototypeParameterQuerySection()
  private val placeholderSection = new PrototypeParameterPlaceholderSection()

  private val removeButton = new Button()
  removeButton.setText("Remove")

  getChildren.addAll(querySection, placeholderSection, removeButton)

  def setQuery(query: String): Unit = querySection.setQuery(query)

  def setOnParameterQueryChanged(parameterQueryChangedHandler: String => Unit): Unit = querySection.setOnParameterQueryChanged(parameterQueryChangedHandler)

  def setPlaceholder(placeholder: String): Unit = placeholderSection.setPlaceholder(placeholder)

  def setOnParameterPlaceholderChanged(parameterPlaceholderChangedHandler: String => Unit): Unit = placeholderSection.setOnParameterPlaceholderChanged(parameterPlaceholderChangedHandler)

  def setOnRemoveParameterButtonClicked(callback: () => Unit): Unit = {
    removeButton.setOnMouseClicked(event => {
      callback()
    })
  }
}
