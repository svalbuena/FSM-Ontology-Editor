package application.command.state.modify

/**
  * Enumeration of the state's types
  */
object StateType extends Enumeration {
  type StateType = Value
  val INITIAL, SIMPLE, FINAL, INITIAL_FINAL = Value
}
