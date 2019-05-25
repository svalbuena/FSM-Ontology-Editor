package domain.element.action

/**
  * Enumeration of the action's types
  */
object ActionType extends Enumeration {
  type ActionType = Value
  val ENTRY, EXIT, GUARD = Value
}