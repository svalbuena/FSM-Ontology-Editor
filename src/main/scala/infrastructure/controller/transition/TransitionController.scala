package infrastructure.controller.transition

import infrastructure.controller.DrawingPaneController
import infrastructure.elements.guard.Guard
import infrastructure.elements.transition.Transition
import infrastructure.id.IdGenerator
import infrastructure.toolbox.section.selector.mouse.NormalMouseSelector
import javafx.scene.input.MouseButton

class TransitionController(transition: Transition, drawingPaneController: DrawingPaneController, idGenerator: IdGenerator) {
  private val toolBox = drawingPaneController.toolBox

  private val propertiesBox = transition.propertiesBox
  private val shape = transition.shape

  shape.setOnMouseClicked(event => {
    event.consume()

    event.getButton match {
      case MouseButton.PRIMARY =>
        toolBox.getSelectedTool match {
          case _: NormalMouseSelector =>
            drawingPaneController.propertiesBox.setContent(propertiesBox)
          case _ =>

        }

      case _ =>
    }
  })

  propertiesBox.setOnTransitionNameChanged(transitionName => {
    //TODO: notify the model
    println("Transition name changed to -> " + transitionName)
    transition.name = transitionName
  })

  propertiesBox.setOnAddTransitionGuardButtonClicked(() => {
    //TODO: notify the model
    println("Adding a guard")
    addGuard()
  })

  def addGuard(): Unit = {
    val id = "Guard" + idGenerator.getId
    val guard = new Guard(id)

    transition.guards = guard :: transition.guards

    drawingPaneController.addGuardToTransition(guard, transition)
  }
}

object TransitionController {
  def addTransitionToFsm(): Unit = {

  }

  def modifyTransitionName(): Unit = {

  }

  def removeTransitionFromFsm(): Unit = {

  }
}
