package infrastructure.propertybox.body

import infrastructure.element.body.BodyType.BodyType
import infrastructure.propertybox.body.section.{BodyContentSection, BodyNameSection, BodyTypeSection}
import javafx.scene.layout.VBox

class BodyPropertiesBox extends VBox {
  private val bodyNameSection = new BodyNameSection()
  private val bodyTypeSection = new BodyTypeSection()
  private val bodyContentSection = new BodyContentSection()

  getChildren.addAll(bodyNameSection, bodyTypeSection, bodyContentSection)


  def setBodyName(content: String): Unit = bodyNameSection.setBodyName(content)

  def setOnBodyNameChanged(bodyContentChangedHandler: String => Unit): Unit = bodyNameSection.setOnBodyNameChanged(bodyContentChangedHandler)

  def setBodyType(bodyType: BodyType): Unit = bodyTypeSection.setBodyType(bodyType)

  def setOnBodyTypeChanged(bodyTypeChangedHandler: BodyType => Unit): Unit = bodyTypeSection.setOnBodyTypeChanged(bodyTypeChangedHandler)

  def setBodyContent(content: String): Unit = bodyContentSection.setBodyContent(content)

  def setOnBodyContentChanged(bodyContentChangedHandler: String => Unit): Unit = bodyContentSection.setOnBodyContentChanged(bodyContentChangedHandler)
}
