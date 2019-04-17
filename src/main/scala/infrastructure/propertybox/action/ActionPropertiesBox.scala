package infrastructure.propertybox.action

import infrastructure.elements.action.ActionType.ActionType
import infrastructure.elements.action.MethodType.MethodType
import infrastructure.elements.action.UriType.UriType
import infrastructure.propertybox.action.section.{ActionTimeoutSection, MethodSection, NameTypeSection, UriSection}
import infrastructure.propertybox.body.BodyPropertiesBox
import infrastructure.propertybox.prototypeuri.PrototypeUriPropertiesBox
import javafx.scene.control.TitledPane
import javafx.scene.layout.VBox

class ActionPropertiesBox(private val bodyPropertiesBox: BodyPropertiesBox, prototypeUriPropertiesBox: PrototypeUriPropertiesBox) extends TitledPane {
  private val actionPropertiesBoxPane = new VBox()

  private val nameTypeSection = new NameTypeSection()
  private val methodTypeSection = new MethodSection()
  private val uriSection = new UriSection(prototypeUriPropertiesBox)
  private val timeoutSection = new ActionTimeoutSection()

  actionPropertiesBoxPane.getChildren.addAll(nameTypeSection, methodTypeSection, uriSection, timeoutSection, bodyPropertiesBox)

  setContent(actionPropertiesBoxPane)


  def setTiltedPaneName(actionName: String): Unit = setText(actionName)

  def setActionType(actionType: ActionType): Unit = nameTypeSection.setActionType(actionType)

  def setActionName(actionName: String): Unit = nameTypeSection.setActionName(actionName)

  def setMethodType(methodType: MethodType): Unit = methodTypeSection.setMethodType(methodType)

  def setUriType(uriType: UriType): Unit = uriSection.setUriType(uriType)

  def setAbsoluteUri(absoluteUri: String): Unit = uriSection.setAbsoluteUri(absoluteUri)

  def setOnActionNameChanged(actionNameChangedHandler: String => Unit): Unit = nameTypeSection.setOnActionNameChanged(actionNameChangedHandler)

  def setOnUriTypeChanged(uriTypeChangedHandler: UriType => Unit): Unit = uriSection.setOnUriTypeChanged(uriTypeChangedHandler)

  def setOnMethodTypeChanged(methodTypeChangedHandler: MethodType => Unit): Unit = methodTypeSection.setOnMethodTypeChanged(methodTypeChangedHandler)

  def setOnAbsoluteUriChanged(absoluteUriChangedHandler: String => Unit): Unit = uriSection.setOnAbsoluteUriChanged(absoluteUriChangedHandler)

  def setOnRemoveActionButtonClicked(callback: () => Unit): Unit = nameTypeSection.setOnRemoveActionButtonClicked(callback)

  def setTimeout(timeout: Int): Unit = timeoutSection.setTimeout(timeout)

  def setOnTimeoutChanged(timeoutChangedHandler: Int => Unit): Unit = timeoutSection.setOnTimeoutChanged(timeoutChangedHandler)
}
