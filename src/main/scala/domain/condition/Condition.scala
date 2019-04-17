package domain.condition

import domain.exception.DomainError
import domain.{Element, Environment}

class Condition(name: String,
                private var _query: String = ""
               ) extends Element(name) {

  def this() = this(Environment.generateUniqueName("condition"))

  def query: String = _query

  def query_=(newQuery: String): Either[DomainError, String] = {
    query = newQuery
    Right(query)
  }
}
