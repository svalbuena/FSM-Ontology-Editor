package domain.element.action

import domain.element.action.ActionType.ActionType
import domain.element.action.MethodType.MethodType
import domain.element.action.UriType.UriType
import domain.exception.{ActionTypeError, DomainError, InvalidTimeoutError}
import domain.element.Element
import domain.environment.Environment

/**
  *
  * @param name         name of the action
  * @param _actionType  type of the action
  * @param _methodType  method of the action
  * @param body         body of the action
  * @param _absoluteUri absolute uri of the action
  * @param _uriType     uri type of the action
  * @param prototypeUri prototype uri of the action
  * @param _timeout     timeout of the action
  */
class Action(name: String,
             private var _actionType: ActionType,
             private var _methodType: MethodType = MethodType.GET,
             val body: Body, private var _absoluteUri: String = "https://www.example.com/endpoint1",
             private var _uriType: UriType = UriType.ABSOLUTE,
             val prototypeUri: PrototypeUri,
             private var _timeout: Int = 0,
             environment: Environment
            ) extends Element(name, environment) {

  def this(actionType: ActionType, environment: Environment) = this(name = environment.generateUniqueName("action"), actionType, prototypeUri = new PrototypeUri(environment), body = new Body(environment), environment = environment)

  /**
    * Changes the type of the action, error if swapping between guard and entry or exit or between entry or exit and guard
    *
    * @param newActionType new type of the action
    * @return exception or the new action type
    */
  def actionType_=(newActionType: ActionType): Either[DomainError, ActionType] = {
    if (actionType == ActionType.GUARD && (newActionType == ActionType.ENTRY || newActionType == ActionType.EXIT)) Left(new ActionTypeError("A guard action can't be converted to an entry or exit action"))
    if ((actionType == ActionType.ENTRY || actionType == ActionType.EXIT) && newActionType == ActionType.GUARD) Left(new ActionTypeError("An entry or exit action can't be converted to a guard action"))

    _actionType = newActionType
    Right(actionType)
  }

  def actionType: ActionType = _actionType

  /**
    *
    * @param newMethodType new method type
    * @return exception or the new method type
    */
  def methodType_=(newMethodType: MethodType): Either[DomainError, MethodType] = {
    _methodType = newMethodType
    Right(methodType)
  }

  def methodType: MethodType = _methodType

  /**
    *
    * @param newUriType new uri type
    * @return exception or the new uri type
    */
  def uriType_=(newUriType: UriType): Either[DomainError, UriType] = {
    _uriType = newUriType
    Right(uriType)
  }

  def uriType: UriType = _uriType

  /**
    *
    * @param newAbsoluteUri new absolute uri
    * @return exception or the new absolute uri
    */
  def absoluteUri_=(newAbsoluteUri: String): Either[DomainError, String] = {
    if (!newAbsoluteUri.isEmpty && !newAbsoluteUri.isBlank && !newAbsoluteUri.contains(" ")) {
      _absoluteUri = newAbsoluteUri
      Right(absoluteUri)
    } else Left(new DomainError("The absolute uri can't be empty or contain spaces"))
  }

  def absoluteUri: String = _absoluteUri

  /**
    * Changes the action timeout, error if the timeout is not a number or it is negative
    *
    * @param newTimeout the new timeout
    * @return exception or the timeout
    */
  def timeout_=(newTimeout: String): Either[DomainError, Int] = {
    if (!newTimeout.isEmpty && !newTimeout.isBlank && newTimeout.forall(_.isDigit)) {
      if (newTimeout.toInt >= 0) {
        _timeout = newTimeout.toInt
        Right(timeout)
      } else {
        Left(new InvalidTimeoutError("Timeout value can't be negative"))
      }
    } else {
      Left(new InvalidTimeoutError("Timeout value must be an integer equal or bigger than 0"))
    }
  }

  def timeout: Int = _timeout

  /**
    *
    * @return all the names contained in an action and its children
    */
  def getChildrenNames: List[String] = List(body.name, prototypeUri.name) ::: prototypeUri.getChildrenNames
}
