package infrastructure.propertybox

import javafx.geometry.Pos
import javafx.scene.control.{Button, Label}
import javafx.scene.layout.HBox

class LabelButtonsSection extends HBox {
  private val label = new Label()

  private val button = new Button()

  getChildren.addAll(label, button)

  setStyle()


  def setLabelText(labelText: String): Unit = label.setText(labelText)

  def setButtonText(buttonText: String): Unit = button.setText(buttonText)

  def setButtonCallback(callback: () => Unit): Unit = button.setOnAction(_ => callback())

  private def setStyle(): Unit = {
    setAlignment(Pos.CENTER_LEFT)
  }
}
