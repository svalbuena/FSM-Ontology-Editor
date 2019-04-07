package infrastructure.propertybox.action

import infrastructure.elements.action.ActionType.ActionType
import infrastructure.elements.action.uri.UriType.UriType
import infrastructure.propertybox.action.body.BodyPropertiesBox
import infrastructure.propertybox.action.nametype.NameTypeSection
import infrastructure.propertybox.action.uri.UriSection
import infrastructure.propertybox.action.uri.prototype.PrototypeUriPropertiesBox
import javafx.scene.control.TitledPane
import javafx.scene.layout.VBox

class ActionPropertiesBox(private val bodyPropertiesBox: BodyPropertiesBox, prototypeUriPropertiesBox: PrototypeUriPropertiesBox) extends TitledPane {
  private val actionPropertiesBoxPane = new VBox()

  private val nameTypeSection = new NameTypeSection()
  private val uriSection = new UriSection(prototypeUriPropertiesBox)

  actionPropertiesBoxPane.getChildren.addAll(nameTypeSection, uriSection, bodyPropertiesBox)

  setContent(actionPropertiesBoxPane)


  def setTiltedPaneName(actionName: String): Unit = setText(actionName)

  def setActionType(actionType: ActionType): Unit = nameTypeSection.setActionType(actionType)

  def setActionName(actionName: String): Unit = nameTypeSection.setActionName(actionName)

  def setUriType(uriType: UriType): Unit = uriSection.setUriType(uriType)

  def setAbsoluteUri(absoluteUri: String): Unit = uriSection.setAbsoluteUri(absoluteUri)

  def setOnActionNameChanged(actionNameChangedHandler: String => Unit): Unit = nameTypeSection.setOnActionNameChanged(actionNameChangedHandler)

  def setOnUriTypeChanged(uriTypeChangedHandler: UriType => Unit): Unit = uriSection.setOnUriTypeChanged(uriTypeChangedHandler)

  def setOnAbsoluteUriChanged(absoluteUriChangedHandler: String => Unit): Unit = uriSection.setOnAbsoluteUriChanged(absoluteUriChangedHandler)

  def setOnRemoveActionButtonClicked(callback: () => Unit): Unit = nameTypeSection.setOnRemoveActionButtonClicked(callback)
}
