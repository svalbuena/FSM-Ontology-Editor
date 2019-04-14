package infrastructure.elements.transition

import infrastructure.drawingpane.shape.transition.TransitionShape
import infrastructure.elements.Element
import infrastructure.elements.guard.Guard
import infrastructure.elements.node._
import infrastructure.propertybox.transition.TransitionPropertiesBox
import javafx.scene.layout.Pane

class Transition(id: String,
                 var name: String = "Transition",
                 val source: ConnectableElement,
                 val destination: ConnectableElement,
                 var guards: List[Guard] = List()
                ) extends Element(id) {
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
