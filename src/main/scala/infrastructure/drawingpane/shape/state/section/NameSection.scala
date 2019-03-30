package infrastructure.drawingpane.shape.state.section

import javafx.geometry.{Insets, Pos}
import javafx.scene.control.TextField
import javafx.scene.layout.StackPane

class NameSection(name: String) extends StackPane {
  val Padding = 10.0

  getStyleClass.add("title-area")
  setPadding(new Insets(Padding))

  val nameTextField = new TextField()
  nameTextField.setAlignment(Pos.CENTER)
  nameTextField.setText(name)

  nameTextField.prefWidthProperty.bind(widthProperty)
  nameTextField.prefHeightProperty.bind(heightProperty)

  getChildren.add(nameTextField)

  def getName: String = {
    nameTextField.getText
  }

  def setName(name: String): Unit = {
    nameTextField.setText(name)
  }
}
