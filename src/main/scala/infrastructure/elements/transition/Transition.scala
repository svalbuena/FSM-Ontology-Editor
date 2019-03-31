package infrastructure.elements.transition

import infrastructure.drawingpane.shape.{ConnectableShape, Shape, TransitionShape}
import infrastructure.elements.Element
import infrastructure.elements.node.{ConnectableElement, End, GhostElement, Start, State}

class Transition(id: String, val source: ConnectableElement, val destination: ConnectableElement) extends Element(id) {
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
}
