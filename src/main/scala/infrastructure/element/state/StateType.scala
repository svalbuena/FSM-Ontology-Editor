package infrastructure.element.state

object StateType extends Enumeration {
  type StateType = Value
  val INITIAL, SIMPLE, FINAL, INITIAL_FINAL = Value
}