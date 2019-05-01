package domain.exception

/**
  *
  * @param message message of the error
  */
class DomainError(message: String = "") extends Exception(message) {

}
