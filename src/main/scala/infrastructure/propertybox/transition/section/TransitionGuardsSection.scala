package infrastructure.propertybox.transition.section

import infrastructure.propertybox.guard.GuardPropertiesBox
import javafx.scene.control.{Button, Label}
import javafx.scene.layout.{HBox, VBox}

class TransitionGuardsSection extends VBox {
  private val transitionGuardsTitleLabel = new Label()
  transitionGuardsTitleLabel.setText("Guards")

  private val addTransitionGuardButton = new Button()
  addTransitionGuardButton.setText("Add guard")

  private val titleAndButtonPane = new HBox()
  titleAndButtonPane.getChildren.addAll(transitionGuardsTitleLabel, addTransitionGuardButton)

  private val transitionGuardsPane = new VBox()

  getChildren.addAll(titleAndButtonPane, transitionGuardsPane)

  def addTransitionGuard(guardPropertiesBox: GuardPropertiesBox): Unit = transitionGuardsPane.getChildren.add(guardPropertiesBox)
  def removeTransitionGuard(guardPropertiesBox: GuardPropertiesBox): Unit = transitionGuardsPane.getChildren.remove(guardPropertiesBox)
  def setOnAddTransitionGuardButtonClicked(callback: () => Unit): Unit = {
    addTransitionGuardButton.setOnMouseClicked(event => {
      callback()
    })
  }
}
