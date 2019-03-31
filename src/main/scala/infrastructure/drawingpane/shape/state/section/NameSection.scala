package infrastructure.drawingpane.shape.state.section

import javafx.geometry.{Insets, Pos}
import javafx.scene.control.TextField
import javafx.scene.layout.StackPane

class NameSection(name: String) extends StackPane {
  val Padding = 10.0

  getStyleClass.add("title-area")
  setPadding(new Insets(Padding))

  val nameLabel = new TextField()
  nameLabel.setAlignment(Pos.CENTER)
  nameLabel.setText(name)

  nameLabel.prefWidthProperty.bind(widthProperty)
  nameLabel.prefHeightProperty.bind(heightProperty)

  getChildren.add(nameLabel)

  def getName: String = {
    nameLabel.getText
  }

  def setName(name: String): Unit = {
    nameLabel.setText(name)
  }
}
