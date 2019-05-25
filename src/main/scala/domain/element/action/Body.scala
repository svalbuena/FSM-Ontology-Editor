package domain.element.action

import domain.element.action.BodyType.BodyType
import domain.exception.DomainError
import domain.element.Element
import domain.environment.Environment

/**
  *
  * @param name      name of the body
  * @param _bodyType type of the body
  * @param _content  content of the body
  */
class Body(name: String,
           private var _bodyType: BodyType = BodyType.RDF,
           private var _content: String = "",
           environment: Environment
          ) extends Element(name, environment) {

  def this(environment: Environment) = this(environment.generateUniqueName("body"), environment = environment)

  /**
    *
    * @param newBodyType new body type
    * @return exception or the body type
    */
  def bodyType_=(newBodyType: BodyType): Either[DomainError, BodyType] = {
    _bodyType = newBodyType
    Right(bodyType)
  }

  def bodyType: BodyType = _bodyType

  /**
    *
    * @param newContent new content of the body
    * @return exception or the content
    */
  def content_=(newContent: String): Either[DomainError, String] = {
    _content = newContent
    Right(content)
  }

  def content: String = _content
}
