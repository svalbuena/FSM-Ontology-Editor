package infrastructure.propertybox.guard.section

import javafx.scene.control.Button
import javafx.scene.layout.HBox

class GuardButtonsSection extends HBox {
  private val addConditionButton = new Button()
  addConditionButton.setText("Add condition")

  private val addActionButton = new Button()
  addActionButton.setText("Add action")

  getChildren.addAll(addConditionButton, addActionButton)


  def setOnAddConditionButtonClicked(callback: () => Unit): Unit = {
    addConditionButton.setOnMouseClicked(event => {
      callback()
    })
  }

  def setOnAddActionButtonClicked(callback: () => Unit): Unit = {
    addActionButton.setOnMouseClicked(event => {
      callback()
    })
  }
}
