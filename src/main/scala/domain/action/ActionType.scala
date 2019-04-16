package domain.action

object ActionType extends Enumeration {
  type ActionType = Value
  val ENTRY, EXIT, GUARD = Value
}