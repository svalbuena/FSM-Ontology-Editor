package infrastructure.propertybox

import javafx.geometry.Pos
import javafx.scene.control.{ComboBox, Label}
import javafx.scene.layout.HBox

/**
  * Template for a section with a label and a combobox
  *
  * @tparam T
  */
class ComboBoxSection[T] extends HBox {
  private val label = new Label()
  private val comboBox = new ComboBox[T]()

  getChildren.addAll(label, comboBox)

  setStyle()

  def setLabelText(labelText: String): Unit = {
    label.setText(labelText)
  }

  def setItems(items: List[T]): Unit = {
    comboBox.getItems.clear()
    items.foreach(comboBox.getItems.add)
  }

  def setSelection(item: T): Unit = comboBox.getSelectionModel.select(item)

  def setOnSelectionChanged(selectionChangedHandler: T => Unit): Unit = {
    comboBox.valueProperty().addListener(_ => {
      selectionChangedHandler(comboBox.getValue)
    })
  }

  private def setStyle(): Unit = {
    getStyleClass.add("properties-box-hbox")

    setAlignment(Pos.CENTER_LEFT)
  }
}
