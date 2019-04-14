package infrastructure.propertybox.action.section.body.section

import infrastructure.elements.action.body.BodyType
import infrastructure.elements.action.body.BodyType.BodyType
import javafx.scene.control.{ComboBox, Label}
import javafx.scene.layout.HBox

class BodyTypeSection() extends HBox {
  private val bodyTypeLabel = new Label()
  bodyTypeLabel.setText("Body type:")

  private val bodyTypeComboBox = new ComboBox[BodyType]()
  bodyTypeComboBox.getItems.addAll(
    BodyType.RDF,
    BodyType.JSON,
    BodyType.SPARQL
  )

  getChildren.addAll(bodyTypeLabel, bodyTypeComboBox)


  def setBodyType(bodyType: BodyType): Unit = bodyTypeComboBox.getSelectionModel.select(bodyType)

  def setOnBodyTypeChanged(bodyTypeChangedHandler: BodyType => Unit): Unit = {
    bodyTypeComboBox.valueProperty().addListener(observable => {
      bodyTypeChangedHandler(bodyTypeComboBox.getValue)
    })
  }
}
