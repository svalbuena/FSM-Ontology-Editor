package domain.state

object StateType extends Enumeration {
  type StateType = Value
  val INITIAL, SIMPLE, FINAL = Value
}