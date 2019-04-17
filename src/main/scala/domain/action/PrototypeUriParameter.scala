package domain.action

import domain.exception.DomainError
import domain.{Element, Environment}

class PrototypeUriParameter(name: String,
                            private var _placeholder: String = "",
                            private var _query: String = ""
                           ) extends Element(name) {

  def this() = this(Environment.generateUniqueName("prototypeUriParameter"))

  def placeholder: String = _placeholder

  def placeholder_=(newPlaceholder: String): Either[DomainError, String] = {
    placeholder = newPlaceholder
    Right(placeholder)
  }

  def query: String = _query

  def query_=(newQuery: String): Either[DomainError, String] = {
    query = newQuery
    Right(query)
  }
}
