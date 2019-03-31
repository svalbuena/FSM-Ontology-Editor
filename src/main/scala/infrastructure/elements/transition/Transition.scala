package infrastructure.elements.transition

import infrastructure.drawingpane.shape.{ConnectableShape, Shape, TransitionShape}
import infrastructure.elements.node.{ConnectableElement, End, GhostElement, Start, State}

class Transition(val source: ConnectableElement, val destination: ConnectableElement) {
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
