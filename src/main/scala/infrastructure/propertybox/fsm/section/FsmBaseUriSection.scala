package infrastructure.propertybox.fsm.section

import javafx.scene.control.{Label, TextField}
import javafx.scene.layout.HBox

class FsmBaseUriSection extends HBox {
  private val baseUriLabel = new Label()
  baseUriLabel.setText("Base URI:")

  private val baseUriTextField = new TextField()


  getChildren.addAll(baseUriLabel, baseUriTextField)


  def setBaseUri(baseUri: String): Unit = baseUriTextField.setText(baseUri)

  def setOnBaseUriChanged(baseUriChangedHandler: String => Unit): Unit = {
    baseUriTextField.setOnKeyTyped(_ => {
      baseUriChangedHandler(baseUriTextField.getText)
    })
  }
}
