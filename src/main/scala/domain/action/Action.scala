package domain.action

import domain.action.ActionType.ActionType
import domain.action.MethodType.MethodType
import domain.action.UriType.UriType
import domain.exception.{ActionTypeError, DomainError, InvalidTimeoutError}
import domain.{Element, Environment}

class Action(name: String,
             private var _actionType: ActionType,
             private var _methodType: MethodType = MethodType.GET,
             val body: Body, private var _absoluteUri: String = "",
             private var _uriType: UriType = UriType.ABSOLUTE,
             val prototypeUri: PrototypeUri,
             private var _timeout: Int = 0) extends Element(name) {

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

  def timeout_=(newTimeout: String): Either[DomainError, Int] = {
    if (newTimeout.forall(_.isDigit)) {
      if (newTimeout.toInt < 0) {
        _timeout = newTimeout.toInt
        Right(timeout)

      } else {
        Left(new InvalidTimeoutError("Timeout value can't be negative"))
      }
    } else {
      Left(new InvalidTimeoutError("Timeout value must be an integer"))
    }
  }

  def getChildrenNames: List[String] = List(body.name, prototypeUri.name) ::: prototypeUri.getChildrenNames
}
