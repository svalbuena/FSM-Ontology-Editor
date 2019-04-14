package infrastructure.elements.guard

import infrastructure.drawingpane.shape.guard.GuardPane
import infrastructure.elements.Element
import infrastructure.elements.action.Action
import infrastructure.elements.condition.Condition
import infrastructure.elements.transition.Transition
import infrastructure.propertybox.guard.GuardPropertiesBox

class Guard(id: String,
            var name: String = "Guard",
            var actions: List[Action] = List(),
            var conditions: List[Condition] = List()
           ) extends Element(id) {
  val propertiesBox = new GuardPropertiesBox()
  val shape = new GuardPane()

  var parent: Option[Transition] = None


  def hasParent: Boolean = parent.isDefined

  def setParent(transition: Transition): Unit = parent = Some(transition)

  def getParent: Transition = parent.get
}
