package domain.action

import domain.action.BodyType.BodyType
import domain.exception.DomainError
import domain.{Element, Environment}

/**
  *
  * @param name      name of the body
  * @param _bodyType type of the body
  * @param _content  content of the body
  */
class Body(name: String,
           private var _bodyType: BodyType = BodyType.RDF,
           private var _content: String = ""
          ) extends Element(name) {

  def this() = this(Environment.generateUniqueName("body"))

  def bodyType: BodyType = _bodyType

  /**
    *
    * @param newBodyType new body type
    * @return exception or the body type
    */
  def bodyType_=(newBodyType: BodyType): Either[DomainError, BodyType] = {
    bodyType = newBodyType
    Right(bodyType)
  }

  def content: String = _content

  /**
    *
    * @param newContent new content of the body
    * @return exception or the content
    */
  def content_=(newContent: String): Either[DomainError, String] = {
    content = newContent
    Right(content)
  }
}
