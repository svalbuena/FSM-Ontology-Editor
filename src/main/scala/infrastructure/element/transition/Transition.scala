package infrastructure.element.transition

import infrastructure.drawingpane.shape.transition.TransitionShape
import infrastructure.element.end.End
import infrastructure.element.fsm.FiniteStateMachine
import infrastructure.element.ghostnode.GhostNode
import infrastructure.element.guard.Guard
import infrastructure.element.start.Start
import infrastructure.element.state.State
import infrastructure.element.{ConnectableElement, Element}
import infrastructure.propertybox.transition.TransitionPropertiesBox
import javafx.scene.layout.Pane

/**
  * Transition data
  *
  * @param name        name of the transition
  * @param source      source of the transition
  * @param destination destination of the transition
  * @param isEditable  if the transition can be edited, to add guards, etc
  * @param parent      the parent of the transitions
  */
class Transition(name: String,
                 val source: ConnectableElement,
                 val destination: ConnectableElement,
                 var isEditable: Boolean = false,
                 val parent: FiniteStateMachine
                ) extends Element(name) {

  val propertiesBox = new TransitionPropertiesBox()
  val shape = new TransitionShape()
  var guards: List[Guard] = List()

  def getSourceShape: Pane = getShape(source)

  def getDestinationShape: Pane = getShape(destination)

  private def getShape(connectableElement: ConnectableElement): Pane = {
    connectableElement match {
      case state: State => state.shape
      case start: Start => start.shape
      case end: End => end.shape
      case ghostNode: GhostNode => ghostNode.shape
    }
  }

  def addGuard(guard: Guard): Unit = {
    guards = guard :: guards

    propertiesBox.addTransitionGuard(guard.propertiesBox, guard.name)
    shape.addTransitionGuard(guard.shape)
  }

  def removeGuard(guard: Guard): Unit = {
    guards = guards.filterNot(g => g == guard)

    propertiesBox.removeTransitionGuard(guard.propertiesBox)
    shape.removeTransitionGuard(guard.shape)
  }
}
