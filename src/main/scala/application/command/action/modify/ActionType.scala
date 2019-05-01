package application.command.action.modify

/**
  * Enumeration for the action's types
  */
object ActionType extends Enumeration {
  type ActionType = Value
  val ENTRY, EXIT, GUARD = Value
}