package infrastructure.propertybox.action.uri

import infrastructure.elements.action.uri.UriType.UriType
import infrastructure.elements.action.uri.UriType
import infrastructure.elements.action.uri.prototype.PrototypeUri
import infrastructure.propertybox.action.uri.`type`.UriTypeSection
import infrastructure.propertybox.action.uri.absolute.AbsoluteUriSection
import infrastructure.propertybox.action.uri.prototype.PrototypeUriPropertiesBox
import javafx.scene.control.{ComboBox, Label}
import javafx.scene.layout.{HBox, VBox}

class UriSection(private val prototypeUriPropertiesBox: PrototypeUriPropertiesBox) extends VBox {
  private val uriTypeSection = new UriTypeSection()
  private val absoluteUriSection = new AbsoluteUriSection()

  getChildren.addAll(uriTypeSection, absoluteUriSection, prototypeUriPropertiesBox)


  def setUriType(uriType: UriType): Unit = {
    uriTypeSection.setUriType(uriType)

    if (uriType == UriType.ABSOLUTE) {
      absoluteUriSection.setDisable(false)
      prototypeUriPropertiesBox.setDisable(true)
    } else {
      absoluteUriSection.setDisable(true)
      prototypeUriPropertiesBox.setDisable(false)
    }
  }

  def setOnUriTypeChanged(uriTypeChangedHandler: UriType => Unit): Unit = uriTypeSection.setOnUriTypeChanged(uriTypeChangedHandler)
  def setAbsoluteUri(absoluteUri: String): Unit = absoluteUriSection.setAbsoluteUri(absoluteUri)
  def setOnAbsoluteUriChanged(absoluteUriChangedHandler: String => Unit): Unit = absoluteUriSection.setOnAbsoluteUriChanged(absoluteUriChangedHandler)
}
