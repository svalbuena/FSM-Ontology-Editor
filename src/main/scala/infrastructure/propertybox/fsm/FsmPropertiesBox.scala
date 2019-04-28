package infrastructure.propertybox.fsm

import infrastructure.propertybox.LabelTextFieldSection
import javafx.scene.control.Label
import javafx.scene.layout.VBox

class FsmPropertiesBox extends VBox {
  private val propertiesBoxLabel = new Label()
  propertiesBoxLabel.setText("Finite State Machine")

  private val fsmNameSection = new LabelTextFieldSection
  fsmNameSection.setLabelText("Name:")

  private val fsmBaseUriSection = new LabelTextFieldSection
  fsmBaseUriSection.setLabelText("Base URI:")

  getChildren.addAll(propertiesBoxLabel, fsmNameSection, fsmBaseUriSection)

  setConstraints()


  def setFsmName(fsmName: String): Unit = fsmNameSection.setText(fsmName)
  def setOnFsmNameChanged(fsmNameChangedHandler: String => Unit): Unit = fsmNameSection.setOnTextChanged(fsmNameChangedHandler)

  def setBaseUri(baseUri: String): Unit = fsmBaseUriSection.setText(baseUri)
  def setOnBaseUriChanged(baseUriChangedHandler: String => Unit): Unit = fsmBaseUriSection.setOnTextChanged(baseUriChangedHandler)

  private def setConstraints(): Unit = {
    propertiesBoxLabel.prefWidthProperty().bind(widthProperty())
    fsmNameSection.prefWidthProperty().bind(widthProperty())
    fsmBaseUriSection.prefWidthProperty().bind(widthProperty())
  }
}
