package infrastructure.elements.transition

import infrastructure.drawingpane.shape.transition.TransitionShape
import infrastructure.drawingpane.shape.{ConnectableShape, Shape}
import infrastructure.elements.Element
import infrastructure.elements.guard.Guard
import infrastructure.elements.node.{ConnectableElement, End, GhostElement, Start, State}
import infrastructure.propertybox.transition.TransitionPropertiesBox

class Transition(id: String, var name: String, val source: ConnectableElement, val destination: ConnectableElement, var guards: List[Guard]) extends Element(id) {
  val propertiesBox = new TransitionPropertiesBox()
  val shape = new TransitionShape()

  def getSourceShape: ConnectableShape = getShape(source)
  def getDestinationShape: ConnectableShape = getShape(destination)

  private def getShape(connectableElement: ConnectableElement): ConnectableShape = {
    connectableElement match {
      case state: State => state.shape
      case start: Start => start.shape
      case end: End => end.shape
      case ghostElement: GhostElement => ghostElement.shape
    }
  }

  def this(id: String, name: String, source: ConnectableElement, destination: ConnectableElement) = this(id, name, source, destination, List())
}
