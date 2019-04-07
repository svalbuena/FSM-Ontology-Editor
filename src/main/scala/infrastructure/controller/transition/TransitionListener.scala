package infrastructure.controller.transition

import infrastructure.controller.InfrastructureController
import infrastructure.elements.condition.Condition
import infrastructure.elements.guard.Guard
import infrastructure.elements.transition.Transition
import infrastructure.id.IdGenerator
import infrastructure.toolbox.section.selector.mouse.NormalMouseSelector
import javafx.scene.input.MouseButton

class TransitionListener(transition: Transition, infrastructureController: InfrastructureController, idGenerator: IdGenerator) {
  private val toolBox = infrastructureController.toolBox

  private val propertiesBox = transition.propertiesBox
  private val shape = transition.shape

  shape.setOnMouseClicked(event => {
    event.consume()

    event.getButton match {
      case MouseButton.PRIMARY =>
        toolBox.getSelectedTool match {
          case _: NormalMouseSelector =>
            infrastructureController.propertiesBox.setContent(propertiesBox)
          case _ =>

        }

      case _ =>
    }
  })

  propertiesBox.setOnTransitionNameChanged(transitionName => {
    //TODO: notify the model
    transition.name = transitionName
  })

  propertiesBox.setOnAddTransitionGuardButtonClicked(() => {
    addGuard()
  })

  def addGuard(): Unit = {
    val id = idGenerator.getId
    val guard = new Guard(id, "Guard" + id)

    transition.guards = guard :: transition.guards

    infrastructureController.addGuardToTransition(guard, transition)
  }
}
