package infrastructure.propertybox.transition

import infrastructure.propertybox.guard.GuardPropertiesBox
import infrastructure.propertybox.{LabelTextFieldSection, LabelVBoxSection}
import javafx.scene.layout.VBox

/**
  * Properties box of a transition
  */
class TransitionPropertiesBox extends VBox {
  private val transitionNameSection = new LabelTextFieldSection
  transitionNameSection.setLabelText("Name:")

  private val transitionGuardsSection = new LabelVBoxSection[GuardPropertiesBox]("transition-guard-titled-pane")
  transitionGuardsSection.setLabelText("Guards")
  transitionGuardsSection.setButtonText("Add guard")

  getChildren.addAll(transitionNameSection, transitionGuardsSection)

  setStyle()


  def setTransitionName(name: String): Unit = transitionNameSection.setText(name)

  def setOnTransitionNameChanged(transitionNameChangedHandler: String => Unit): Unit = transitionNameSection.setOnTextChanged(transitionNameChangedHandler)

  def addTransitionGuard(guardPropertiesBox: GuardPropertiesBox, title: String): Unit = transitionGuardsSection.addPane(guardPropertiesBox, title)

  def removeTransitionGuard(guardPropertiesBox: GuardPropertiesBox): Unit = transitionGuardsSection.removePane(guardPropertiesBox)

  def setGuardPropertiesBoxTitle(guardPropertiesBox: GuardPropertiesBox, title: String): Unit = transitionGuardsSection.setPaneTitle(guardPropertiesBox, title)

  def setOnAddTransitionGuardButtonClicked(callback: () => Unit): Unit = transitionGuardsSection.setButtonCallback(callback)

  private def setStyle(): Unit = {
    getStyleClass.add("properties-box-vbox")
  }
}
