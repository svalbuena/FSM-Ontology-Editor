package domain

import domain.action.{Action, Body, PrototypeUri, PrototypeUriParameter}
import domain.condition.Condition
import domain.exception.{DomainError, ElementNotFoundError, FsmNotSelectedError, NameNotUniqueError}
import domain.fsm.FiniteStateMachine
import domain.guard.Guard
import domain.repository.{FsmRepository, Properties}
import domain.state.State
import domain.transition.Transition

/**
  * In-memory representation of all the data that the model needs to maintain
  */
class Environment(private val fsmRepository: FsmRepository) {
  private val FsmPrefix = "file:///D:/projects/ontologies/fsm/fsm#"
  private val HttpPrefix = "http://www.w3.org/2011/http#"
  private val HttpMethodsPrefix = "http://www.w3.org/2011/http-methods#"
  private val GeometryPrefix = "http://data.ign.fr/ontologies/geometrie#"
  private val properties = new Properties(FsmPrefix, HttpPrefix, HttpMethodsPrefix, GeometryPrefix)
  private val idGenerator = new IdGenerator()
  private var selectedFsmOption: Option[FiniteStateMachine] = None
  private var nameList: List[String] = List()
  private var fsmList: List[FiniteStateMachine] = List()

  /**
    * Saves the fsm to a file, error if the fsm is not selected
    *
    * @param filename path to the file to store the fsm
    * @return exception or nothing if successful
    */
  def saveFsm(filename: String): Either[Exception, _] = {
    if (selectedFsmOption.isDefined) {
      val fsm = selectedFsmOption.get
      fsmRepository.saveFsm(fsm, properties, filename)
    } else {
      Left(new FsmNotSelectedError)
    }
  }

  /**
    * Loads an fsm from a file, error if the file doesn't exist or there is some error on the syntax
    *
    * @param filename filename where the fsm is stored
    * @return exception or an fsm instance with te data loaded
    */
  def loadFsm(filename: String): Either[Exception, FiniteStateMachine] = {
    fsmRepository.loadFsm(properties, filename) match {
      case Left(error) => Left(error)
      case Right(fsm) =>
        addFsm(fsm)
        Right(fsm)
    }
  }

  /**
    * Adds an fsm to the environment, error if the fsm name is not unique
    *
    * @param fsm fsm to be added
    * @return exception or nothing if successful
    */
  def addFsm(fsm: FiniteStateMachine): Either[DomainError, _] = {
    if (isNameUnique(fsm.name)) {
      fsmList = fsm :: fsmList
      (fsm.name :: fsm.getChildrenNames).foreach(addName)
      Right(())
    } else {
      Left(new NameNotUniqueError(s"Error -> Name '${fsm.name} is not unique"))
    }
  }

  def isNameUnique(name: String): Boolean = !isNameRepeated(name)

  def isNameRepeated(name: String): Boolean = nameList.contains(name)

  def addName(name: String): Unit = nameList = name :: nameList

  /**
    * Selects the fsm to use, error if the fsm to select doesn't exist
    *
    * @param name name of the fsm to select
    * @return exception or name of the selected fsm
    */
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

  /**
    * Removes an fsm from the environment, error if the fsm doesn't belong to the environment
    *
    * @param fsm fsm to be removed
    * @return exception or nothing if successful
    */
  def removeFsm(fsm: FiniteStateMachine): Either[DomainError, _] = {
    if (fsmList.contains(fsm)) {
      fsmList = fsmList.filterNot(f => f == fsm)
      (fsm.name :: fsm.getChildrenNames).foreach(removeName)
      Right(())
    } else {
      Left(new ElementNotFoundError("Fsm not found"))
    }
  }

  def removeName(name: String): Unit = nameList = nameList.filterNot(n => n.equals(name))

  /**
    * Generates an unique name for an element
    *
    * @param prefix the prefix of the new unique name
    * @return new unique name
    */
  def generateUniqueName(prefix: String): String = {
    var name = ""

    do {
      name = prefix + idGenerator.getId
    } while (nameList.contains(name))

    name
  }

  /**
    * Returns the current selected fsm, error if none fsm is selected
    *
    * @return exception or the selected fsm
    */
  def getSelectedFsm: Either[DomainError, FiniteStateMachine] = {
    if (selectedFsmOption.isDefined) {
      val fsm = selectedFsmOption.get

      Right(fsm)
    } else Left(new FsmNotSelectedError)
  }

  /**
    * Returns an action with the specified name, error if an fsm is not selected or if it doesn't exist
    *
    * @param actionName name of the action
    * @return exception or the action
    */
  def getAction(actionName: String): Either[DomainError, Action] = {
    if (selectedFsmOption.isDefined) {
      val fsm = selectedFsmOption.get

      val actions = fsm.states.flatMap(_.actions) ::: fsm.transitions.flatMap(_.guards.flatMap(_.actions))
      val actionIndex  = actions.indexWhere(_.name.equals(actionName))

      if (actionIndex != -1) Right(actions(actionIndex))
      else Left(new ElementNotFoundError("Action not found"))
    } else Left(new FsmNotSelectedError)
  }

  /**
    * Returns a state with the specified name, error if an fsm is not selected or if it doesn't exist
    *
    * @param stateName name of the state
    * @return exception or the state
    */
  def getState(stateName: String): Either[DomainError, State] = {
    if (selectedFsmOption.isDefined) {
      val fsm = selectedFsmOption.get

      val states = fsm.states
      val stateIndex  = states.indexWhere(_.name.equals(stateName))

      if (stateIndex != -1) Right(states(stateIndex))
      else Left(new ElementNotFoundError("State not found"))
    } else Left(new FsmNotSelectedError)
  }

  /**
    * Returns a transition with the specified name, error if an fsm is not selected or if it doesn't exist
    *
    * @param transitionName name of the transition
    * @return exception or the transition
    */
  def getTransition(transitionName: String): Either[DomainError, Transition] = {
    if (selectedFsmOption.isDefined) {
      val fsm = selectedFsmOption.get

      val transitions = fsm.transitions
      val transitionIndex  = transitions.indexWhere(_.name.equals(transitionName))

      if (transitionIndex != -1) Right(transitions(transitionIndex))
      else Left(new ElementNotFoundError("Transition not found"))
    } else Left(new FsmNotSelectedError)
  }

  /**
    * Returns a guard with the specified name, error if an fsm is not selected or if it doesn't exist
    *
    * @param guardName name of the guard
    * @return exception or the guard
    */
  def getGuard(guardName: String): Either[DomainError, Guard] = {
    if (selectedFsmOption.isDefined) {
      val fsm = selectedFsmOption.get

      println("Searching for -> " + guardName)

      val guards = fsm.transitions.flatMap(_.guards)
      val guardIndex  = guards.indexWhere(_.name.equals(guardName))

      if (guardIndex != -1) Right(guards(guardIndex))
      else Left(new ElementNotFoundError("Guard not found"))
    } else Left(new FsmNotSelectedError)
  }

  /**
    * Returns a condition with the specified name, error if an fsm is not selected or if it doesn't exist
    *
    * @param conditionName name of the condition
    * @return exception or the condition
    */
  def getCondition(conditionName: String): Either[DomainError, Condition] = {
    if (selectedFsmOption.isDefined) {
      val fsm = selectedFsmOption.get

      val conditions = fsm.transitions.flatMap(_.guards.flatMap(_.conditions))
      val conditionIndex  = conditions.indexWhere(_.name.equals(conditionName))

      if (conditionIndex != -1) Right(conditions(conditionIndex))
      else Left(new ElementNotFoundError("Condition not found"))
    } else Left(new FsmNotSelectedError)
  }

  /**
    * Returns a body with the specified name, error if an fsm is not selected or if it doesn't exist
    *
    * @param bodyName name of the body
    * @return exception or the body
    */
  def getBody(bodyName: String): Either[DomainError, Body] = {
    if (selectedFsmOption.isDefined) {
      val fsm = selectedFsmOption.get

      val bodies = fsm.states.flatMap(_.actions.map(_.body)) ::: fsm.transitions.flatMap(_.guards.flatMap(_.actions.map(_.body)))
      val bodyIndex  = bodies.indexWhere(body => body.name.equals(bodyName))

      if (bodyIndex != -1) Right(bodies(bodyIndex))
      else Left(new ElementNotFoundError("Body not found"))
    } else Left(new FsmNotSelectedError)
  }

  /**
    * Returns a prototype uri with the specified name, error if an fsm is not selected or if it doesn't exist
    *
    * @param prototypeUriName name of the prototype uri
    * @return exception or the prototype uri
    */
  def getPrototypeUri(prototypeUriName: String): Either[DomainError, PrototypeUri] = {
    if (selectedFsmOption.isDefined) {
      val fsm = selectedFsmOption.get

      val prototypes = fsm.states.flatMap(_.actions.map(_.prototypeUri)) ::: fsm.transitions.flatMap(_.guards.flatMap(_.actions.map(_.prototypeUri)))
      val prototypeIndex  = prototypes.indexWhere(prototype => prototype.name.equals(prototypeUriName))

      if (prototypeIndex != -1) Right(prototypes(prototypeIndex))
      else Left(new ElementNotFoundError("PrototypeUri not found"))
    } else Left(new FsmNotSelectedError)
  }

  /**
    * Returns a parameter with the specified name, error if an fsm is not selected or if it doesn't exist
    *
    * @param prototypeUriParameterName name of the parameter
    * @return exception or the parameter
    */
  def getPrototypeUriParameter(prototypeUriParameterName: String): Either[DomainError, PrototypeUriParameter] = {
    if (selectedFsmOption.isDefined) {
      val fsm = selectedFsmOption.get

      val parameters = fsm.states.flatMap(_.actions.flatMap(_.prototypeUri.prototypeUriParameters)) ::: fsm.transitions.flatMap(_.guards.flatMap(_.actions.flatMap(_.prototypeUri.prototypeUriParameters)))
      val parameterIndex = parameters.indexWhere(parameter => parameter.name.equals(prototypeUriParameterName))

      if (parameterIndex != -1) Right(parameters(parameterIndex))
      else Left(new ElementNotFoundError("PrototypeUriParameter not found"))
    } else Left(new FsmNotSelectedError)
  }
}
