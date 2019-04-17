package infrastructure.element.condition

import infrastructure.drawingpane.shape.condition.ConditionPane
import infrastructure.element.Element
import infrastructure.element.guard.Guard
import infrastructure.propertybox.condition.ConditionPropertiesBox

class Condition(name: String,
                var query: String = ""
               ) extends Element(name) {
  val propertiesBox = new ConditionPropertiesBox()
  val shape = new ConditionPane()

  var parent: Option[Guard] = None

  def hasParent: Boolean = parent.isDefined

  def setParent(guard: Guard): Unit = parent = Some(guard)

  def getParent: Guard = parent.get
}
