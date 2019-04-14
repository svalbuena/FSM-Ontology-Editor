package infrastructure.propertybox.action.section

import javafx.scene.control.{Label, TextField}
import javafx.scene.layout.HBox

class ActionTimeoutSection extends HBox {
  private val timeoutLabel = new Label()
  timeoutLabel.setText("Timeout (ms):")

  private val timeoutTextField = new TextField()

  getChildren.addAll(timeoutLabel, timeoutTextField)

  def setTimeout(timeout: Int): Unit = {
    timeoutTextField.setText(timeout.toString)
  }

  def setOnTimeoutChanged(timeoutChangedHandler: Int => Unit): Unit = {
    timeoutTextField.setOnKeyTyped(event => {
      val text = timeoutTextField.getText()
      if (text.matches("\\d*")) {
        timeoutChangedHandler(timeoutTextField.getText.toInt)
      } else {
        println("Invalid format")
      }
    })
  }
}
