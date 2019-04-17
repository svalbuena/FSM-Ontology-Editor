package infrastructure.propertybox.body

import infrastructure.elements.body.BodyType.BodyType
import infrastructure.propertybox.body.section.{BodyContentSection, BodyTypeSection}
import javafx.scene.layout.VBox

class BodyPropertiesBox extends VBox {
  private val bodyTypeSection = new BodyTypeSection()
  private val bodyContentSection = new BodyContentSection()

  getChildren.addAll(bodyTypeSection, bodyContentSection)


  def setBodyType(bodyType: BodyType): Unit = bodyTypeSection.setBodyType(bodyType)

  def setOnBodyTypeChanged(bodyTypeChangedHandler: BodyType => Unit): Unit = bodyTypeSection.setOnBodyTypeChanged(bodyTypeChangedHandler)

  def setBodyContent(content: String): Unit = bodyContentSection.setBodyContent(content)

  def setOnBodyContentChanged(bodyContentChangedHandler: String => Unit): Unit = bodyContentSection.setOnBodyContentChanged(bodyContentChangedHandler)
}
