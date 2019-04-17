package infrastructure.element.guard

import infrastructure.drawingpane.shape.guard.GuardPane
import infrastructure.element.Element
import infrastructure.element.action.Action
import infrastructure.element.condition.Condition
import infrastructure.element.transition.Transition
import infrastructure.propertybox.guard.GuardPropertiesBox

class Guard(name: String,
            var actions: List[Action] = List(),
            var conditions: List[Condition] = List()
           ) extends Element(name) {
  val propertiesBox = new GuardPropertiesBox()
  val shape = new GuardPane()

  var parent: Option[Transition] = None


  def hasParent: Boolean = parent.isDefined

  def setParent(transition: Transition): Unit = parent = Some(transition)

  def getParent: Transition = parent.get
}
