package domain.exception

/**
  *
  * @param message message of the error
  */
class FsmNotSelectedError(message: String = "No fsm is selected") extends DomainError(message) {

}
