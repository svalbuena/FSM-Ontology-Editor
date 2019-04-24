package infrastructure.propertybox.fsm

import infrastructure.propertybox.fsm.section.{FsmBaseUriSection, FsmNameSection}
import javafx.scene.control.Label
import javafx.scene.layout.VBox

class FsmPropertiesBox extends VBox {
  private val propertiesBoxLabel = new Label()
  propertiesBoxLabel.setText("Finite State Machine")

  private val fsmNameSection = new FsmNameSection

  private val fsmBaseUriSection = new FsmBaseUriSection


  getChildren.addAll(propertiesBoxLabel, fsmNameSection, fsmBaseUriSection)


  def setFsmName(fsmName: String): Unit = fsmNameSection.setFsmName(fsmName)
  def setOnFsmNameChanged(fsmNameChangedHandler: String => Unit): Unit = fsmNameSection.setOnFsmNameChanged(fsmNameChangedHandler)

  def setBaseUri(baseUri: String): Unit = fsmBaseUriSection.setBaseUri(baseUri)
  def setOnBaseUriChanged(baseUriChangedHandler: String => Unit): Unit = fsmBaseUriSection.setOnBaseUriChanged(baseUriChangedHandler)
}
