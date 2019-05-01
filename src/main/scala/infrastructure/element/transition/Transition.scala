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

class Transition(name: String,
                 val source: ConnectableElement,
                 val destination: ConnectableElement,
                 var isEditable: Boolean = false,
                 val parent: FiniteStateMachine
                ) extends Element(name) {

  var guards: List[Guard] = List()
  val propertiesBox = new TransitionPropertiesBox()
  val shape = new TransitionShape()


  def getSourceShape: Pane = getShape(source)

  def getDestinationShape: Pane = getShape(destination)

  def addGuard(guard: Guard): Unit = {
    guards = guard :: guards

    propertiesBox.addTransitionGuard(guard.propertiesBox, guard.name)
    shape.addTransitionGuard(guard.shape)

    shape
  }

  def removeGuard(guard: Guard): Unit = {
    guards = guards.filterNot(g => g == guard)

    propertiesBox.removeTransitionGuard(guard.propertiesBox)
    shape.removeTransitionGuard(guard.shape)
  }

  private def getShape(connectableElement: ConnectableElement): Pane = {
    connectableElement match {
      case state: State => state.shape
      case start: Start => start.shape
      case end: End => end.shape
      case ghostNode: GhostNode => ghostNode.shape
    }
  }
}
