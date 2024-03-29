package infrastructure.drawingpane.shape.state.section

import javafx.geometry.{Insets, Pos}
import javafx.scene.control.Label
import javafx.scene.layout.StackPane

/**
  * Name section of a state
  */
class NameSection extends StackPane {
  val Padding = 10.0

  getStyleClass.add("title-area")
  setPadding(new Insets(Padding))

  val nameLabel = new Label()
  nameLabel.setAlignment(Pos.CENTER)

  nameLabel.prefWidthProperty.bind(widthProperty)
  nameLabel.prefHeightProperty.bind(heightProperty)

  getChildren.add(nameLabel)


  def setName(name: String): Unit = {
    nameLabel.setText(name)
  }
}
