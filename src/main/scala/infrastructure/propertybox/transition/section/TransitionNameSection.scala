package infrastructure.propertybox.transition.section

import javafx.scene.control.{Label, TextField}
import javafx.scene.layout.HBox

class TransitionNameSection extends HBox {
  val transitionNameLabel = new Label
  transitionNameLabel.setText("Name:")

  val transitionNameTextField = new TextField()

  getChildren.addAll(transitionNameLabel, transitionNameTextField)


  def setTransitionName(name: String): Unit = transitionNameLabel.setText(name)

  def setOnTransitionNameChanged(transitionNameChangedHandler: String => Unit): Unit = {
    transitionNameTextField.setOnKeyTyped(event => {
      transitionNameChangedHandler(transitionNameTextField.getText)
    })
  }
}
