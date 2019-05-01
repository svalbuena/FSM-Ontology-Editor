package domain.condition

import domain.exception.DomainError
import domain.{Element, Environment}

/**
  *
  * @param name   name of the condition
  * @param _query query of the condition
  */
class Condition(name: String,
                private var _query: String = ""
               ) extends Element(name) {

  def this() = this(Environment.generateUniqueName("condition"))

  def query: String = _query

  /**
    *
    * @param newQuery new query
    * @return exception or the query
    */
  def query_=(newQuery: String): Either[DomainError, String] = {
    _query = newQuery
    Right(query)
  }
}
