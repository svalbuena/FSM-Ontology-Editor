package infrastructure.propertybox.transition

import infrastructure.propertybox.LabelTextFieldSection
import infrastructure.propertybox.guard.GuardPropertiesBox
import infrastructure.propertybox.transition.section.{TransitionGuardsSection, TransitionNameSection}
import javafx.scene.control.Label
import javafx.scene.layout.VBox

class TransitionPropertiesBox extends VBox {
  private val transitionTitleLabel = new Label()
  transitionTitleLabel.setText("Transition")

  private val transitionNameSection = new LabelTextFieldSection
  transitionNameSection.setLabelText("Name:")

  private val transitionGuardsSection = new TransitionGuardsSection()

  getChildren.addAll(transitionTitleLabel, transitionNameSection, transitionGuardsSection)

  def setTransitionName(name: String): Unit = transitionNameSection.setText(name)

  def setOnTransitionNameChanged(transitionNameChangedHandler: String => Unit): Unit = transitionNameSection.setOnTextChanged(transitionNameChangedHandler)

  def addTransitionGuard(guardPropertiesBox: GuardPropertiesBox): Unit = transitionGuardsSection.addTransitionGuard(guardPropertiesBox)

  def removeTransitionGuard(guardPropertiesBox: GuardPropertiesBox): Unit = transitionGuardsSection.removeTransitionGuard(guardPropertiesBox)

  def setOnAddTransitionGuardButtonClicked(callback: () => Unit): Unit = transitionGuardsSection.setOnAddTransitionGuardButtonClicked(callback)
}
