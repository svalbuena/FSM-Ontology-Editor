package infrastructure.propertybox.action.uri.`type`

import infrastructure.elements.action.uri.UriType
import infrastructure.elements.action.uri.UriType.UriType
import javafx.scene.control.{ComboBox, Label}
import javafx.scene.layout.HBox

class UriTypeSection extends HBox {
  private val uriTypeLabel = new Label()
  uriTypeLabel.setText("Select the URI type:")

  private val uriTypeComboBox = new ComboBox[UriType]()
  uriTypeComboBox.getItems.addAll(
    UriType.ABSOLUTE,
    UriType.PROTOTYPE
  )

  getChildren.addAll(uriTypeLabel, uriTypeComboBox)


  def setUriType(uriType: UriType): Unit = uriTypeComboBox.getSelectionModel.select(uriType)

  def setOnUriTypeChanged(uriTypeChangedHandler: UriType => Unit): Unit = {
    uriTypeComboBox.valueProperty().addListener(observable => {
      uriTypeChangedHandler(uriTypeComboBox.getValue)
    })
  }
}
