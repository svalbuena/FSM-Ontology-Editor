package infrastructure.propertybox.action.section

import infrastructure.elements.action.{PrototypeUri, UriType}
import infrastructure.elements.action.UriType.UriType
import javafx.scene.control.{ComboBox, Label, ListCell}
import javafx.scene.layout.{HBox, VBox}
import javafx.scene.shape.Rectangle

class UriSection(uriType: UriType, absoluteUri: String, prototypeUri: PrototypeUri) extends VBox {
  private val uriTypeSection = new HBox

  private val uriTypeLabel = new Label()
  uriTypeLabel.setText("Select the URI type:")

  private val uriTypeComboBox = new ComboBox[UriType]()
  uriTypeComboBox.getItems.addAll(
    UriType.ABSOLUTE,
    UriType.PROTOTYPE
  )
  uriTypeComboBox.getSelectionModel.select(uriType)

  uriTypeSection.getChildren.addAll(uriTypeLabel, uriTypeComboBox)


  private val absoluteUriSection = new AbsoluteUriSection(absoluteUri)

  private val prototypeUriSection = new PrototypeUriSection(prototypeUri.structure, prototypeUri.parameters)

  if (uriType == UriType.ABSOLUTE) {
    absoluteUriSection.setDisable(false)
    prototypeUriSection.setDisable(true)
  } else {
    absoluteUriSection.setDisable(true)
    prototypeUriSection.setDisable(false)
  }

  getChildren.addAll(uriTypeSection, absoluteUriSection, prototypeUriSection)
}
