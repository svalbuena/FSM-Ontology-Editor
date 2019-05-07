package domain.condition

import domain.exception.DomainError
import domain.{Element, Environment}

/**
  *
  * @param name   name of the condition
  * @param _query query of the condition
  */
class Condition(name: String,
                private var _query: String = "",
                environment: Environment
               ) extends Element(name, environment) {

  def this(environment: Environment) = this(environment.generateUniqueName("condition"), environment = environment)

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
