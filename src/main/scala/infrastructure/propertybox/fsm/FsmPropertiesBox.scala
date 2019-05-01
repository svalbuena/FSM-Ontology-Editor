package infrastructure.propertybox.fsm

import infrastructure.propertybox.LabelTextFieldSection
import javafx.scene.layout.VBox

/**
  * Properties box of an fsm
  */
class FsmPropertiesBox extends VBox {
  private val fsmNameSection = new LabelTextFieldSection
  fsmNameSection.setLabelText("Name:")

  private val fsmBaseUriSection = new LabelTextFieldSection
  fsmBaseUriSection.setLabelText("Base URI:")

  getChildren.addAll(fsmNameSection, fsmBaseUriSection)

  setStyle()


  def setFsmName(fsmName: String): Unit = fsmNameSection.setText(fsmName)

  def setOnFsmNameChanged(fsmNameChangedHandler: String => Unit): Unit = fsmNameSection.setOnTextChanged(fsmNameChangedHandler)

  def setBaseUri(baseUri: String): Unit = fsmBaseUriSection.setText(baseUri)

  def setOnBaseUriChanged(baseUriChangedHandler: String => Unit): Unit = fsmBaseUriSection.setOnTextChanged(baseUriChangedHandler)

  private def setStyle(): Unit = {
    getStyleClass.add("properties-box-vbox")

    fsmNameSection.prefWidthProperty().bind(widthProperty())
    fsmBaseUriSection.prefWidthProperty().bind(widthProperty())
  }
}
