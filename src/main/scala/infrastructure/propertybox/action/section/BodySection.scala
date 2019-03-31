package infrastructure.propertybox.action.section


import infrastructure.elements.action.{Body, BodyType}
import infrastructure.elements.action.BodyType.BodyType
import javafx.scene.control.{ComboBox, Label, TextField}
import javafx.scene.layout.{HBox, VBox}

class BodySection(body: Body) extends VBox {
  private val bodyTypeSection = new HBox()

  private val bodyTypeLabel = new Label()
  bodyTypeLabel.setText("Body type:")

  private val bodyTypeComboBox = new ComboBox[BodyType]()
  bodyTypeComboBox.getItems.addAll(
    BodyType.RDF,
    BodyType.JSON,
    BodyType.SPARQL
  )
  bodyTypeComboBox.getSelectionModel.select(body.bodyType)

  bodyTypeSection.getChildren.addAll(bodyTypeLabel, bodyTypeComboBox)


  private val contentPane = new VBox()

  private val contentLabel = new Label()
  contentLabel.setText("Content:")

  private val contentTextField = new TextField()
  contentTextField.setText(body.content)

  contentPane.getChildren.addAll(contentLabel, contentTextField)


  getChildren.addAll(bodyTypeSection, contentPane)
}
