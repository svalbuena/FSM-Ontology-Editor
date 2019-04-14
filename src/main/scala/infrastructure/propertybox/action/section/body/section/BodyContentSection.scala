package infrastructure.propertybox.action.section.body.section

import javafx.scene.control.{Label, TextField}
import javafx.scene.layout.VBox

class BodyContentSection() extends VBox {
  private val contentLabel = new Label()
  contentLabel.setText("Content:")

  private val contentTextField = new TextField()

  getChildren.addAll(contentLabel, contentTextField)


  def setBodyContent(content: String): Unit = contentTextField.setText(content)

  def setOnBodyContentChanged(bodyContentChangedHandler: String => Unit): Unit = {
    contentTextField.setOnKeyTyped(event => {
      bodyContentChangedHandler(contentTextField.getText)
    })
  }
}
