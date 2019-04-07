package infrastructure.propertybox.transition

import infrastructure.propertybox.guard.GuardPropertiesBox
import infrastructure.propertybox.state.section.{StateActionsSection, StateNameSection}
import infrastructure.propertybox.transition.section.{TransitionGuardsSection, TransitionNameSection}
import javafx.scene.control.Label
import javafx.scene.layout.VBox

class TransitionPropertiesBox extends VBox {
  private val transitionTitleLabel = new Label()
  transitionTitleLabel.setText("Transition")

  private val transitionNameSection = new TransitionNameSection()
  private val transitionGuardsSection = new TransitionGuardsSection()

  getChildren.addAll(transitionTitleLabel, transitionNameSection, transitionGuardsSection)

  def setTransitionName(name: String): Unit = transitionNameSection.setTransitionName(name)
  def setOnTransitionNameChanged(transitionNameChangedHandler: String => Unit): Unit = transitionNameSection.setOnTransitionNameChanged(transitionNameChangedHandler)
  def addTransitionGuard(guardPropertiesBox: GuardPropertiesBox): Unit = transitionGuardsSection.addTransitionGuard(guardPropertiesBox)
  def removeTransitionGuard(guardPropertiesBox: GuardPropertiesBox): Unit = transitionGuardsSection.removeTransitionGuard(guardPropertiesBox)
  def setOnAddTransitionGuardButtonClicked(callback: () => Unit): Unit = transitionGuardsSection.setOnAddTransitionGuardButtonClicked(callback)
}
