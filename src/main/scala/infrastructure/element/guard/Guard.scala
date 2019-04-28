package infrastructure.element.guard

import infrastructure.drawingpane.shape.guard.GuardPane
import infrastructure.element.Element
import infrastructure.element.action.Action
import infrastructure.element.condition.Condition
import infrastructure.element.transition.Transition
import infrastructure.propertybox.guard.GuardPropertiesBox

class Guard(name: String,
            val parent: Transition
           ) extends Element(name) {

  var actions: List[Action] = List()
  var conditions: List[Condition] = List()

  val propertiesBox = new GuardPropertiesBox()
  val shape = new GuardPane()


  def addAction(action: Action): Unit = {
    actions = action :: actions

    propertiesBox.addAction(action.propertiesBox, action.name)
    shape.addAction(action.shape)

    //TODO: fix this
    //canvas.updateTransitionGuardGroupPosition(guard.getParent.shape)
  }

  def removeAction(action: Action): Unit = {
    actions = actions.filterNot(a => a == action)

    propertiesBox.removeAction(action.propertiesBox)
    shape.removeAction(action.shape)
  }

  def addCondition(condition: Condition): Unit = {
    conditions = condition :: conditions

    propertiesBox.addCondition(condition.propertiesBox, condition.name)
    shape.addCondition(condition.shape)

    //TODO: fix this
    //canvas.updateTransitionGuardGroupPosition(guard.getParent.shape)
  }

  def removeCondition(condition: Condition): Unit = {
    conditions = conditions.filterNot(c => c == condition)

    propertiesBox.removeCondition(condition.propertiesBox)
    shape.removeCondition(condition.shape)
  }
}
