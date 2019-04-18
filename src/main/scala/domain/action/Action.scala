package domain.action

import domain.action.ActionType.ActionType
import domain.action.MethodType.MethodType
import domain.action.UriType.UriType
import domain.exception.{ActionTypeError, DomainError, InvalidTimeoutError}
import domain.{Element, Environment}

class Action(name: String,
             private var _actionType: ActionType,
             private var _methodType: MethodType = MethodType.GET,
             private var _uriType: UriType = UriType.ABSOLUTE,
             private var _absoluteUri: String = "",
             private var _timeout: Int = 0,
             val prototypeUri: PrototypeUri,
             val body: Body
            ) extends Element(name) {

  def this(actionType: ActionType) = this(name = Environment.generateUniqueName("action"), actionType, prototypeUri = new PrototypeUri(), body = new Body())

  def actionType: ActionType = _actionType

  def actionType_=(newActionType: ActionType): Either[DomainError, ActionType] = {
    if (actionType == ActionType.GUARD && (newActionType == ActionType.ENTRY || newActionType == ActionType.EXIT)) Left(new ActionTypeError("A guard action can't be converted to an entry or exit action"))
    if ((actionType == ActionType.ENTRY || actionType == ActionType.EXIT) && newActionType == ActionType.GUARD) Left(new ActionTypeError("An entry or exit action can't be converted to a guard action"))

    _actionType = newActionType
    Right(actionType)
  }

  def methodType: MethodType = _methodType

  def methodType_=(newMethodType: MethodType): Either[DomainError, MethodType] = {
    _methodType = newMethodType
    Right(methodType)
  }

  def uriType: UriType = _uriType

  def uriType_=(newUriType: UriType): Either[DomainError, UriType] = {
    _uriType = newUriType
    Right(uriType)
  }

  def absoluteUri: String = _absoluteUri

  def absoluteUri_=(newAbsoluteUri: String): Either[DomainError, String] = {
    _absoluteUri = newAbsoluteUri
    Right(absoluteUri)
  }

  def timeout: Int = _timeout

  def timeout_=(newTimeout: Int): Either[DomainError, Int] = {
    if (timeout < 0) Left(new InvalidTimeoutError("Timeout value can't be negative"))
    _timeout = newTimeout
    Right(timeout)
  }

  def getChildrenNames: List[String] = List(body.name, prototypeUri.name) ::: prototypeUri.getChildrenNames
}
