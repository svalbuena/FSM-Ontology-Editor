package domain.action

import domain.action.BodyType.BodyType
import domain.exception.DomainError
import domain.{Element, Environment}

class Body(name: String,
           private var _bodyType: BodyType = BodyType.RDF,
           private var _content: String = ""
          ) extends Element(name) {

  def this() = this(Environment.generateUniqueName("body"))

  def bodyType: BodyType = _bodyType

  def bodyType_=(newBodyType: BodyType): Either[DomainError, BodyType] = {
    bodyType = newBodyType
    Right(bodyType)
  }

  def content: String = _content

  def content_=(newContent: String): Either[DomainError, String] = {
    content = newContent
    Right(content)
  }
}
