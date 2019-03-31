package infrastructure.propertybox.action

import infrastructure.elements.action.Action
import infrastructure.propertybox.action.section.{BodySection, NameTypeSection, UriSection}
import javafx.scene.layout.VBox

class ActionPropertiesBox(action: Action) extends VBox {
  private val nameTypeSection = new NameTypeSection(action.actionType, action.name)

  private val uriSection = new UriSection(action.uriType, action.absoluteUri, action.prototypeUri)

  private val bodySection = new BodySection(action.body)

  getChildren.addAll(nameTypeSection, uriSection, bodySection)
}
