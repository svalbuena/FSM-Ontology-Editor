package domain.action

import domain.exception.DomainError
import domain.{Element, Environment}

/**
  *
  * @param name         name of the parameter
  * @param _placeholder placeholder of the parameter
  * @param _query       query of the parameter
  */
class PrototypeUriParameter(name: String,
                            private var _placeholder: String = "",
                            private var _query: String = "",
                            environment: Environment
                           ) extends Element(name, environment) {

  def this(environment: Environment) = this(environment.generateUniqueName("prototypeUriParameter"), environment = environment)

  /**
    *
    * @param newPlaceholder new placeholder
    * @return exception or the placeholder
    */
  def placeholder_=(newPlaceholder: String): Either[DomainError, String] = {
    _placeholder = newPlaceholder
    Right(placeholder)
  }

  def placeholder: String = _placeholder

  /**
    *
    * @param newQuery new query
    * @return exception or the query
    */
  def query_=(newQuery: String): Either[DomainError, String] = {
    _query = newQuery
    Right(query)
  }

  def query: String = _query
}
