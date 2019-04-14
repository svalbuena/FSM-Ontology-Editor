package infrastructure.elements.condition

import infrastructure.drawingpane.shape.condition.ConditionPane
import infrastructure.elements.Element
import infrastructure.elements.guard.Guard
import infrastructure.propertybox.condition.ConditionPropertiesBox

class Condition(id: String,
                var name: String = "Condition",
                var query: String = ""
               ) extends Element(id) {
  val propertiesBox = new ConditionPropertiesBox()
  val shape = new ConditionPane()

  var parent: Option[Guard] = None

  def hasParent: Boolean = parent.isDefined

  def setParent(guard: Guard): Unit = parent = Some(guard)

  def getParent: Guard = parent.get
}
