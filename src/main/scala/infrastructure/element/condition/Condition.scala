package infrastructure.element.condition

import infrastructure.drawingpane.shape.condition.ConditionPane
import infrastructure.element.Element
import infrastructure.element.guard.Guard
import infrastructure.propertybox.condition.ConditionPropertiesBox

/**
  * Condition data
  * @param name name of the condition
  * @param query query of the condition
  * @param parent parent of the condition
  */
class Condition(name: String,
                var query: String = "",
                val parent: Guard
               ) extends Element(name) {

  val propertiesBox = new ConditionPropertiesBox()
  val shape = new ConditionPane()
}
