package infrastructure.element.transition

import infrastructure.drawingpane.shape.transition.TransitionShape
import infrastructure.element.end.End
import infrastructure.element.ghostnode.GhostElement
import infrastructure.element.guard.Guard
import infrastructure.element.start.Start
import infrastructure.element.state.State
import infrastructure.element.{ConnectableElement, Element}
import infrastructure.propertybox.transition.TransitionPropertiesBox
import javafx.scene.layout.Pane

class Transition(name: String,
                 val source: ConnectableElement,
                 val destination: ConnectableElement,
                 var guards: List[Guard] = List()
                ) extends Element(name) {
  val propertiesBox = new TransitionPropertiesBox()
  val shape = new TransitionShape()

  def getSourceShape: Pane = getShape(source)

  def getDestinationShape: Pane = getShape(destination)

  private def getShape(connectableElement: ConnectableElement): Pane = {
    connectableElement match {
      case state: State => state.shape
      case start: Start => start.shape
      case end: End => end.shape
      case ghostElement: GhostElement => ghostElement.shape
    }
  }
}
