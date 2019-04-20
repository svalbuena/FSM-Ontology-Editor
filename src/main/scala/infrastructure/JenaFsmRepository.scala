package infrastructure

import java.io.{BufferedReader, FileNotFoundException, FileReader}

import domain.FsmRepository
import domain.action.ActionType.ActionType
import domain.action.BodyType.BodyType
import domain.action.MethodType.MethodType
import domain.action.UriType.UriType
import domain.action._
import domain.condition.Condition
import domain.fsm.FiniteStateMachine
import domain.guard.Guard
import domain.state.StateType.StateType
import domain.state.{State, StateType}
import domain.transition.Transition
import org.apache.jena.rdf.model.impl.PropertyImpl
import org.apache.jena.rdf.model.{Model, ModelFactory, Resource}
import org.apache.jena.reasoner.ReasonerRegistry
import org.apache.jena.util.FileManager
import org.apache.jena.vocabulary.ReasonerVocabulary

class JenaFsmRepository extends FsmRepository {
  val FsmPrefix = "file:///D:/projects/ontologies/fsm/fsm.owl#"
  val HttpPrefix = "http://www.w3.org/2011/http#"
  val RdfPrefix = "http://www.w3.org/1999/02/22-rdf-syntax-ns#"

  val Contains = new PropertyImpl(FsmPrefix + "contains")
  val HasEntryAction = new PropertyImpl(FsmPrefix + "hasEntryAction")
  val HasExitAction = new PropertyImpl(FsmPrefix + "hasExitAction")
  val HasBody = new PropertyImpl(FsmPrefix + "hasBody")
  val HasPrototypeUri = new PropertyImpl(FsmPrefix + "hasPrototypeURI")
  val HasBodyType = new PropertyImpl(FsmPrefix + "hasBodyType")
  val HasBodyContent = new PropertyImpl(FsmPrefix + "hasContent")
  val HasTimeoutInMs = new PropertyImpl(FsmPrefix + "hasTimeoutInMs")
  val HasParameter = new PropertyImpl(FsmPrefix + "hasParameter")
  val HasStructure = new PropertyImpl(FsmPrefix + "hasStructure")
  val HasPlaceholder = new PropertyImpl(FsmPrefix + "hasPlaceholder")
  val HasQuery = new PropertyImpl(FsmPrefix + "hasQuery")
  val HasSourceState = new PropertyImpl(FsmPrefix + "hasSourceState")
  val HasTargetState = new PropertyImpl(FsmPrefix + "hasTargetState")
  val HasTransitionGuard = new PropertyImpl(FsmPrefix + "hasTransitionGuard")
  val HasGuardCondition = new PropertyImpl(FsmPrefix + "hasGuardCondition")
  val HasGuardAction = new PropertyImpl(FsmPrefix + "hasGuardAction")
  val HasContent = new PropertyImpl(FsmPrefix + "hasContent")


  val HasMethod = new PropertyImpl(HttpPrefix + "mthd")
  val HasAbsoluteUri = new PropertyImpl(HttpPrefix + "absoluteURI")

  val TransitionClass = new PropertyImpl(FsmPrefix + "Transition")
  val StateClass = new PropertyImpl(FsmPrefix + "State")

  val Type = new PropertyImpl(RdfPrefix + "type")

  override def saveFsm(fsm: FiniteStateMachine, filename: String): Either[Exception, _] = {
    Right(())
  }

  override def loadFsm(filename: String): Either[Exception, FiniteStateMachine] = {
    val fsmBaseUri = "file:///D:/projects/ontologies/siot/demo_siot.owl"
    val fsmUri = "file:///D:/projects/ontologies/siot/demo_siot.owl#siot_fsm"

    getBufferedReaderFromFile(filename) match {
      case Left(error) =>
        println(error)
        Left(error)
      case Right(bufferedReader) =>
        val fsmModel = FileManager.get().loadModel(filename, "TURTLE")
        val fsmSchema = FileManager.get().loadModel("D:\\projects\\ontologies\\fsm\\fsm.owl", "TURTLE")
        val fsmInferredModel = ModelFactory.createRDFSModel(fsmSchema, fsmModel)
        val fsm = readFsm(fsmInferredModel, fsmBaseUri, fsmUri)

        Right(fsm)
    }
  }

  private def getBufferedReaderFromFile(filename: String): Either[Exception, BufferedReader] = {
    try {
      val fileReader = new FileReader(filename)
      val bufferedReader = new BufferedReader(fileReader)
      Right(bufferedReader)
    } catch {
      case e: FileNotFoundException => Left(e)
    }
  }

  private def readFsm(model: Model, fsmBaseUri: String, fsmUri: String): FiniteStateMachine = {
    val fsmRes = model.getResource(fsmUri)

    //First retrieve the states
    var states: List[State] = List()
    fsmRes.listProperties(Contains).forEachRemaining(property => {
      val resource = property.getResource
      if (resource.hasProperty(Type, StateClass)) {
        val state = getStateFromResource(resource)
        states = state :: states
      }
    })

    //Then retrieve the transitions
    var transitions: List[Transition] = List()
    fsmRes.listProperties(Contains).forEachRemaining(property => {
      val resource = property.getResource
      if (resource.hasProperty(Type, TransitionClass)) {
        val transition = getTransitionFromResource(resource, states)
        transitions = transition :: transitions
      }
    })

    new FiniteStateMachine(name = fsmRes.getLocalName, states = states, transitions = transitions)
  }

  private def getTransitionFromResource(transitionRes: Resource, states: List[State]): Transition = {
    var sourceState = new State(0, 0)
    if (transitionRes.hasProperty(HasSourceState)) {
      val sourceStateName = transitionRes.getProperty(HasSourceState).getResource.getLocalName
      for (state <- states) {
        if (state.name.equals(sourceStateName)) sourceState = state
      }
    }

    var destinationState = new State(0, 0)
    if (transitionRes.hasProperty(HasTargetState)) {
      val destinationStateName = transitionRes.getProperty(HasTargetState).getResource.getLocalName
      for (state <- states) {
        if (state.name.equals(destinationStateName)) destinationState = state
      }
    }

    var guards: List[Guard] = List()
    transitionRes.listProperties(HasTransitionGuard).mapWith(_.getResource).forEachRemaining(guardRes => {
      val guard = getGuardFromResource(guardRes)
      guards = guard :: guards
    })

    new Transition(name = transitionRes.getLocalName, source = sourceState, destination = destinationState, guards = guards)
  }

  private def getGuardFromResource(guardRes: Resource): Guard = {
    var actions: List[Action] = List()
    guardRes.listProperties(HasGuardAction).mapWith(_.getResource).forEachRemaining(actionRes => {
      val action = getActionFromResource(actionRes, ActionType.GUARD)
      actions = action :: actions
    })

    var conditions: List[Condition] = List()
    guardRes.listProperties(HasGuardCondition).mapWith(_.getResource).forEachRemaining(conditionRes => {
      val condition = getConditionFromResource(conditionRes)
      conditions = condition :: conditions
    })

    new Guard(name = guardRes.getLocalName, actions = actions, conditions = conditions)
  }

  private def getConditionFromResource(conditionRes: Resource): Condition = {
    var content = ""
    if (conditionRes.hasProperty(HasContent)) {
      content = conditionRes.getProperty(HasContent).getString
    }

    new Condition(name = conditionRes.getLocalName, _query = content)
  }

  private def getStateFromResource(stateRes: Resource): State = {
    var stateType: StateType = StateType.SIMPLE
    stateRes.listProperties(Type).mapWith(_.getResource).forEachRemaining(classRes => {
      (stateType, getStateTypeFromClassResource(classRes)) match {
        case (StateType.SIMPLE, StateType.INITIAL) => stateType = StateType.INITIAL
        case (StateType.SIMPLE, StateType.FINAL) => stateType = StateType.FINAL
        case (StateType.INITIAL, StateType.FINAL) => stateType = StateType.INITIAL_FINAL
        case (StateType.FINAL, StateType.INITIAL) => stateType = StateType.INITIAL_FINAL
        case (_, _) =>
      }
    })

    var actions: List[Action] = List()
    stateRes.listProperties(HasEntryAction).mapWith(_.getResource).forEachRemaining(actionRes => {
      actions = getActionFromResource(actionRes, ActionType.ENTRY) :: actions
    })
    stateRes.listProperties(HasExitAction).mapWith(_.getResource).forEachRemaining(actionRes => {
      actions = getActionFromResource(actionRes, ActionType.EXIT) :: actions
    })

    new State(stateRes.getLocalName, 0, 0, stateType, actions)
  }

  private def getStateTypeFromClassResource(stateClassRes: Resource): StateType = {
    stateClassRes.getLocalName match {
      case "SimpleState" => StateType.SIMPLE
      case "InitialState" => StateType.INITIAL
      case "FinalState" => StateType.FINAL
      case _ => StateType.SIMPLE
    }
  }

  private def getActionFromResource(actionRes: Resource, actionType: ActionType): Action = {
    var methodType: MethodType = MethodType.GET
    if (actionRes.hasProperty(HasMethod)) {
      methodType = actionRes.getProperty(HasMethod).getResource.getLocalName match {
        case "GET" => MethodType.GET
        case "POST" => MethodType.POST
        case _ => MethodType.GET
      }
    }

    var uriType: UriType = UriType.ABSOLUTE
    var absoluteUri: String = ""
    var prototypeUri: PrototypeUri = new PrototypeUri()
    if (actionRes.hasProperty(HasAbsoluteUri)) {
      uriType = UriType.ABSOLUTE
      absoluteUri = actionRes.getProperty(HasAbsoluteUri).getObject.toString

    } else if (actionRes.hasProperty(HasPrototypeUri)) {
      uriType = UriType.PROTOTYPE
      val prototypeUriRes = actionRes.getProperty(HasPrototypeUri).getResource
      prototypeUri = getPrototypeUriFromResource(prototypeUriRes)
    }

    var timeout: Int = 1000
    if (actionRes.hasProperty(HasTimeoutInMs)) {
      timeout = actionRes.getProperty(HasTimeoutInMs).getInt
    }

    var body: Body = new Body()
    if (actionRes.hasProperty(HasBody)) {
      val bodyRes = actionRes.getProperty(HasBody).getResource
      body = getBodyFromResource(bodyRes)
    }

    new Action(actionRes.getLocalName, actionType, methodType, body, absoluteUri, uriType, prototypeUri, timeout)
  }

  private def getPrototypeUriFromResource(prototypeUriRes: Resource): PrototypeUri = {
    var structure: String = ""
    if (prototypeUriRes.hasProperty(HasStructure)) {
      structure = prototypeUriRes.getProperty(HasStructure).getString
    }

    var parameters: List[PrototypeUriParameter] = List()
    prototypeUriRes.listProperties(HasParameter).mapWith(_.getResource).forEachRemaining(parameterRes => {
      parameters = getPrototypeUriParameterFromResource(parameterRes) :: parameters
    })

    new PrototypeUri(name = prototypeUriRes.getLocalName, _structure = structure, prototypeUriParameters = parameters)
  }

  private def getPrototypeUriParameterFromResource(parameterRes: Resource): PrototypeUriParameter = {
    var placeholder: String = ""
    if (parameterRes.hasProperty(HasPlaceholder)) {
      placeholder = parameterRes.getProperty(HasPlaceholder).getString
    }

    var query: String = ""
    if (parameterRes.hasProperty(HasQuery)) {
      query = parameterRes.getProperty(HasQuery).getString
    }

    new PrototypeUriParameter(name = parameterRes.getLocalName, _placeholder = placeholder, _query = query)
  }

  private def getBodyFromResource(bodyRes: Resource): Body = {
    var bodyType: BodyType = BodyType.RDF
    if (bodyRes.hasProperty(HasBodyType)) {
      bodyType = bodyRes.getProperty(HasBodyType).getResource.getLocalName match {
        case "rdf" => BodyType.RDF
        case "executableSparql" => BodyType.SPARQL
        case "other" => BodyType.JSON
        case _ => BodyType.RDF
      }
    }

    var content = ""
    if (bodyRes.hasProperty(HasBodyContent)) {
      content = bodyRes.getProperty(HasBodyContent).getString
    }

    new Body(bodyRes.getLocalName, bodyType, content)
  }
}
