package infrastructure.propertybox.action.body

import infrastructure.elements.action.body.BodyType.BodyType
import infrastructure.elements.action.body.{Body, BodyType}
import infrastructure.propertybox.action.body.`type`.BodyTypeSection
import infrastructure.propertybox.action.body.content.BodyContentSection
import javafx.scene.control.{ComboBox, Label, TextField}
import javafx.scene.layout.{HBox, VBox}

class BodyPropertiesBox(body: Body) extends VBox {
  private val bodyTypeSection = new BodyTypeSection()
  private val bodyContentSection = new BodyContentSection()

  getChildren.addAll(bodyTypeSection, bodyContentSection)


  def setBodyType(bodyType: BodyType): Unit = bodyTypeSection.setBodyType(bodyType)
  def setOnBodyTypeChanged(bodyTypeChangedHandler: BodyType => Unit): Unit = bodyTypeSection.setOnBodyTypeChanged(bodyTypeChangedHandler)

  def setBodyContent(content: String): Unit = bodyContentSection.setBodyContent(content)
  def setOnBodyContentChanged(bodyContentChangedHandler: String => Unit): Unit = bodyContentSection.setOnBodyContentChanged(bodyContentChangedHandler)
}
