package infrastructure.element.condition

import infrastructure.drawingpane.shape.condition.ConditionPane
import infrastructure.element.Element
import infrastructure.element.guard.Guard
import infrastructure.propertybox.condition.ConditionPropertiesBox

class Condition(name: String,
                var query: String = "",
                val parent: Guard
               ) extends Element(name) {

  val propertiesBox = new ConditionPropertiesBox()
  val shape = new ConditionPane()
}
