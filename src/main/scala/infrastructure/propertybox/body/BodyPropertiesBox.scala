package infrastructure.propertybox.body

import infrastructure.element.body.BodyType
import infrastructure.element.body.BodyType.BodyType
import infrastructure.propertybox.{ComboBoxSection, LabelTextAreaSection, LabelTextFieldSection}
import javafx.scene.layout.VBox

class BodyPropertiesBox extends VBox {
  private val bodyNameSection = new LabelTextFieldSection
  bodyNameSection.setLabelText("Content:")

  private val bodyTypeSection = new ComboBoxSection[BodyType]
  bodyTypeSection.setLabelText("Body type:")
  bodyTypeSection.setItems(List(BodyType.RDF, BodyType.JSON, BodyType.SPARQL))

  private val bodyContentSection = new LabelTextAreaSection
  bodyContentSection.setLabelText("Content:")

  getChildren.addAll(bodyNameSection, bodyTypeSection, bodyContentSection)


  def setBodyName(content: String): Unit = bodyNameSection.setText(content)

  def setOnBodyNameChanged(bodyContentChangedHandler: String => Unit): Unit = bodyNameSection.setOnTextChanged(bodyContentChangedHandler)

  def setBodyType(bodyType: BodyType): Unit = bodyTypeSection.setSelection(bodyType)

  def setOnBodyTypeChanged(bodyTypeChangedHandler: BodyType => Unit): Unit = bodyTypeSection.setOnSelectionChanged(bodyTypeChangedHandler)

  def setBodyContent(content: String): Unit = bodyContentSection.setText(content)

  def setOnBodyContentChanged(bodyContentChangedHandler: String => Unit): Unit = bodyContentSection.setOnTextChanged(bodyContentChangedHandler)
}
