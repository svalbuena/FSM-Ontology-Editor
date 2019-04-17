package infrastructure.propertybox.action.section

import infrastructure.element.action.UriType
import infrastructure.element.action.UriType.UriType
import infrastructure.propertybox.prototypeuri.PrototypeUriPropertiesBox
import javafx.scene.layout.VBox

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
