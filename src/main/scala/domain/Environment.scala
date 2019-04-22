package domain

import domain.action.{Action, Body, PrototypeUri, PrototypeUriParameter}
import domain.condition.Condition
import domain.exception.{DomainError, ElementNotFoundError, FsmNotSelectedError, NameNotUniqueError}
import domain.fsm.FiniteStateMachine
import domain.guard.Guard
import domain.state.State
import domain.transition.Transition
import infrastructure.jena.{JenaFsmRepository, Properties}

object Environment {
  private val FsmPrefix = "file:///D:/projects/ontologies/fsm/fsm.ttl#"
  private val HttpPrefix = "http://www.w3.org/2011/http#"
  private val HttpMethodsPrefix = "http://www.w3.org/2011/http-methods#"
  private val FsmUri = "file:///D:/projects/ontologies/siot/demo_siot.ttl#siot_fsm"

  private var selectedFsmOption: Option[FiniteStateMachine] = None
  private var nameList: List[String] = List()
  private var fsmList: List[FiniteStateMachine] = List()
  private val fsmRepository: FsmRepository = new JenaFsmRepository
  private val properties = new Properties(FsmPrefix, HttpPrefix, HttpMethodsPrefix)

  def saveFsm(filename: String): Either[Exception, _] = {
    if (selectedFsmOption.isDefined) {
      val fsm = selectedFsmOption.get
      fsmRepository.saveFsm(fsm, properties, filename)
    } else {
      Left(new FsmNotSelectedError)
    }
  }

  def loadFsm(filename: String): Either[Exception, FiniteStateMachine] = {
    fsmRepository.loadFsm(FsmUri, properties, filename) match {
      case Left(error) => Left(error)
      case Right(fsm) =>
        addFsm(fsm)
        Right(fsm)
    }
  }

  def addFsm(fsm: FiniteStateMachine): Either[DomainError, _] = {
    if (Environment.isNameUnique(fsm.name)) {
      fsmList = fsm :: fsmList
      (fsm.name :: fsm.getChildrenNames).foreach(Environment.addName)
      Right(())
    } else {
      Left(new NameNotUniqueError(s"Error -> Name '${fsm.name} is not unique"))
    }
  }

  def selectFsm(name: String): Either[DomainError, String] = {
    val fsmOption = fsmList.find(fsm => fsm.name.equals(name))

    if (fsmOption.isDefined) {
      println(s"Select fsm -> ${fsmOption.get.name}")
      selectedFsmOption = Some(fsmOption.get)
      Right(fsmOption.get.name)
    } else {
      Left(new ElementNotFoundError("Fsm not found"))
    }
  }

  def removeFsm(fsm: FiniteStateMachine): Either[DomainError, _] = {
    if (fsmList.contains(fsm)) {
      fsmList = fsmList.filterNot(f => f == fsm)
      (fsm.name :: fsm.getChildrenNames).foreach(Environment.removeName)
      Right(())
    } else {
      Left(new ElementNotFoundError("Fsm not found"))
    }
  }

  def generateUniqueName(prefix: String): String = {
    var name = ""

    do {
      name = prefix + IdGenerator.getId
    } while (nameList.contains(name))

    name
  }

  def getSelectedFsm: Either[DomainError, FiniteStateMachine] = {
    if (selectedFsmOption.isDefined) {
      val fsm = selectedFsmOption.get

      Right(fsm)
    } else Left(new FsmNotSelectedError)
  }

  def getAction(actionName: String): Either[DomainError, Action] = {
    if (selectedFsmOption.isDefined) {
      val fsm = selectedFsmOption.get

      for (action <- fsm.states.flatMap(_.actions) ::: fsm.transitions.flatMap(_.guards.flatMap(_.actions))) {
        if (action.name.equals(actionName)) return Right(action)
      }

      Left(new ElementNotFoundError("Action not found"))
    } else Left(new FsmNotSelectedError)
  }

  def getState(stateName: String): Either[DomainError, State] = {
    if (selectedFsmOption.isDefined) {
      val fsm = selectedFsmOption.get

      for (state <- fsm.states) {
        if (state.name.equals(stateName)) return Right(state)
      }

      Left(new ElementNotFoundError("State not found"))
    } else Left(new FsmNotSelectedError)
  }

  def getTransition(transitionName: String): Either[DomainError, Transition] = {
    if (selectedFsmOption.isDefined) {
      val fsm = selectedFsmOption.get

      for (transition <- fsm.transitions) {
        if (transition.name.equals(transitionName)) return Right(transition)
      }

      Left(new ElementNotFoundError("Transition not found"))
    } else Left(new FsmNotSelectedError)
  }

  def getGuard(guardName: String): Either[DomainError, Guard] = {
    if (selectedFsmOption.isDefined) {
      val fsm = selectedFsmOption.get

      for (guard <- fsm.transitions.flatMap(_.guards)) {
        if (guard.name.equals(guardName)) return Right(guard)
      }

      Left(new ElementNotFoundError("Guard not found"))
    } else Left(new FsmNotSelectedError)
  }

  def getCondition(conditionName: String): Either[DomainError, Condition] = {
    if (selectedFsmOption.isDefined) {
      val fsm = selectedFsmOption.get

      for (condition <- fsm.transitions.flatMap(_.guards.flatMap(_.conditions))) {
        if (condition.name.equals(conditionName)) return Right(condition)
      }

      Left(new ElementNotFoundError("Condition not found"))
    } else Left(new FsmNotSelectedError)
  }

  def getBody(bodyName: String): Either[DomainError, Body] = {
    if (selectedFsmOption.isDefined) {
      val fsm = selectedFsmOption.get

      for (body <- fsm.states.flatMap(_.actions.map(_.body)) ::: fsm.transitions.flatMap(_.guards.flatMap(_.actions.map(_.body)))) {
        if (body.name.equals(bodyName)) return Right(body)
      }

      Left(new ElementNotFoundError("Body not found"))
    } else Left(new FsmNotSelectedError)
  }

  def getPrototypeUri(prototypeUriName: String): Either[DomainError, PrototypeUri] = {
    if (selectedFsmOption.isDefined) {
      val fsm = selectedFsmOption.get

      for (prototypeUri <- fsm.states.flatMap(_.actions.map(_.prototypeUri)) ::: fsm.transitions.flatMap(_.guards.flatMap(_.actions.map(_.prototypeUri)))) {
        if (prototypeUri.name.equals(prototypeUriName)) return Right(prototypeUri)
      }

      Left(new ElementNotFoundError("Body not found"))
    } else Left(new FsmNotSelectedError)
  }


  def getPrototypeUriParameter(prototypeUriParameterName: String): Either[DomainError, PrototypeUriParameter] = {
    if (selectedFsmOption.isDefined) {
      val fsm = selectedFsmOption.get

      for (parameter <- fsm.states.flatMap(_.actions.flatMap(_.prototypeUri.prototypeUriParameters)) ::: fsm.transitions.flatMap(_.guards.flatMap(_.actions.flatMap(_.prototypeUri.prototypeUriParameters)))) {
        if (parameter.name.equals(prototypeUriParameterName)) return Right(parameter)
      }

      Left(new ElementNotFoundError("PrototypeUriParameter not found"))
    } else Left(new FsmNotSelectedError)
  }


  def isNameRepeated(name: String): Boolean = nameList.contains(name)

  def isNameUnique(name: String): Boolean = !isNameRepeated(name)

  def addName(name: String): Unit = nameList = name :: nameList

  def removeName(name: String): Unit = nameList = nameList.filterNot(n => n.equals(name))
}
