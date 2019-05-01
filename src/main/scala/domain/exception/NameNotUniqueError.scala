package domain.exception

/**
  *
  * @param message message of the error
  */
class NameNotUniqueError(name: String = "") extends DomainError(name) {

}
