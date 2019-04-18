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

  def addAction(action: Action): Unit = {
    actions = action :: actions

    propertiesBox.addAction(action.propertiesBox)
    shape.addAction(action.shape)

    if (hasParent) {
      //TODO: fix this
      //canvas.updateTransitionGuardGroupPosition(guard.getParent.shape)
    }
  }

  def removeAction(action: Action): Unit = {
    actions = actions.filterNot(a => a == action)

    propertiesBox.removeAction(action.propertiesBox)
    shape.removeAction(action.shape)
  }

  def addCondition(condition: Condition): Unit = {
    conditions = condition :: conditions

    propertiesBox.addCondition(condition.propertiesBox)
    shape.addCondition(condition.shape)

    if (hasParent) {
      //TODO: fix this
      //canvas.updateTransitionGuardGroupPosition(guard.getParent.shape)
    }
  }

  def removeCondition(condition: Condition): Unit = {
    conditions = conditions.filterNot(c => c == condition)

    propertiesBox.removeCondition(condition.propertiesBox)
    shape.removeCondition(condition.shape)
  }

  def hasParent: Boolean = parent.isDefined

  def setParent(transition: Transition): Unit = parent = Some(transition)

  def getParent: Transition = parent.get
}
