package infrastructure.propertybox.action.uri.prototype.parameter

import infrastructure.propertybox.action.uri.prototype.parameter.placeholder.PrototypeParameterPlaceholderSection
import infrastructure.propertybox.action.uri.prototype.parameter.query.PrototypeParameterQuerySection
import javafx.scene.layout.VBox

class PrototypeParameterPropertiesBox extends VBox {
  private val querySection = new PrototypeParameterQuerySection()
  private val placeholderSection = new PrototypeParameterPlaceholderSection()

  getChildren.addAll(querySection, placeholderSection)


  def setQuery(query: String): Unit = querySection.setQuery(query)
  def setOnParameterQueryChanged(parameterQueryChangedHandler: String => Unit): Unit = querySection.setOnParameterQueryChanged(parameterQueryChangedHandler)
  def setPlaceholder(placeholder: String): Unit = placeholderSection.setPlaceholder(placeholder)
  def setOnParameterPlaceholderChanged(parameterPlaceholderChangedHandler: String => Unit): Unit = placeholderSection.setOnParameterPlaceholderChanged(parameterPlaceholderChangedHandler)
}
