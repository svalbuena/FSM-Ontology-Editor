package infrastructure.propertybox.action

import infrastructure.element.action.ActionType.{ActionType, ENTRY, EXIT, GUARD}
import infrastructure.element.action.MethodType.MethodType
import infrastructure.element.action.UriType.UriType
import infrastructure.element.action.{MethodType, UriType}
import infrastructure.propertybox.body.BodyPropertiesBox
import infrastructure.propertybox.prototypeuri.PrototypeUriPropertiesBox
import infrastructure.propertybox.{ComboBoxSection, LabelButtonSection, LabelTextFieldSection}
import javafx.scene.control.Label
import javafx.scene.layout.VBox

/**
  * Properties box of an Action
  * @param bodyPropertiesBox the properties box of the action's body
  * @param prototypeUriPropertiesBox the properties box of the action's uri
  */
class ActionPropertiesBox(private val bodyPropertiesBox: BodyPropertiesBox, prototypeUriPropertiesBox: PrototypeUriPropertiesBox) extends VBox() {
  private val titleAndRemoveSection = new LabelButtonSection
  titleAndRemoveSection.setLabelText("Action")
  titleAndRemoveSection.setButtonText("Remove")

  private val nameSection = new LabelTextFieldSection()

  private val methodTypeSection = new ComboBoxSection[MethodType]
  methodTypeSection.setLabelText("Method type:")
  methodTypeSection.setItems(List(MethodType.GET, MethodType.POST))

  private val uriTypeSection = new ComboBoxSection[UriType]
  uriTypeSection.setLabelText("URI type:")
  uriTypeSection.setItems(List(UriType.ABSOLUTE, UriType.PROTOTYPE))

  private val absoluteUriSection = new LabelTextFieldSection
  absoluteUriSection.setLabelText("Absolute URI:")

  private val prototypeUriTitle = new Label()
  prototypeUriTitle.setText("Prototype URI")

  private val bodyTitle = new Label()
  bodyTitle.setText("Body")

  private val timeoutSection = new LabelTextFieldSection
  timeoutSection.setLabelText("Timeout (ms):")

  getChildren.addAll(titleAndRemoveSection, nameSection, methodTypeSection, timeoutSection, uriTypeSection, absoluteUriSection, prototypeUriTitle, prototypeUriPropertiesBox, bodyTitle, bodyPropertiesBox)

  setStyle()


  def setOnRemoveActionButtonClicked(callback: () => Unit): Unit = titleAndRemoveSection.setButtonCallback(callback)

  def setActionType(actionType: ActionType): Unit = {
    val typeText = actionType match {
      case ENTRY => "/entry"
      case EXIT => "/exit"
      case GUARD => "/"
    }

    nameSection.setLabelText(typeText)
  }

  def setActionName(actionName: String): Unit = nameSection.setText(actionName)

  def setOnActionNameChanged(actionNameChangedHandler: String => Unit): Unit = nameSection.setOnTextChanged(actionNameChangedHandler)

  def setMethodType(methodType: MethodType): Unit = methodTypeSection.setSelection(methodType)

  def setOnMethodTypeChanged(methodTypeChangedHandler: MethodType => Unit): Unit = methodTypeSection.setOnSelectionChanged(methodTypeChangedHandler)

  def setUriType(uriType: UriType): Unit = {
    uriTypeSection.setSelection(uriType)

    if (uriType == UriType.ABSOLUTE) {
      absoluteUriSection.setDisable(false)
      prototypeUriPropertiesBox.setDisable(true)
    } else {
      absoluteUriSection.setDisable(true)
      prototypeUriPropertiesBox.setDisable(false)
    }
  }

  def setOnUriTypeChanged(uriTypeChangedHandler: UriType => Unit): Unit = uriTypeSection.setOnSelectionChanged(uriTypeChangedHandler)

  def setAbsoluteUri(absoluteUri: String): Unit = absoluteUriSection.setText(absoluteUri)

  def setOnAbsoluteUriChanged(absoluteUriChangedHandler: String => Unit): Unit = absoluteUriSection.setOnTextChanged(absoluteUriChangedHandler)

  def setTimeout(timeout: String): Unit = timeoutSection.setText(timeout)

  def setOnTimeoutChanged(timeoutChangedHandler: String => Unit): Unit = timeoutSection.setOnTextChanged(timeoutChangedHandler)

  private def setStyle(): Unit = {
    getStyleClass.add("properties-box-vbox")
    prototypeUriTitle.getStyleClass.add("properties-h3")
    bodyTitle.getStyleClass.add("properties-h3")
  }
}
