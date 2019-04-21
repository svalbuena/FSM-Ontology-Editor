package infrastructure.jena

import domain.action.ActionType.ActionType
import domain.action.BodyType.BodyType
import domain.action.MethodType.MethodType
import domain.action.{Action, ActionType, Body, BodyType, MethodType, PrototypeUri, PrototypeUriParameter, UriType}
import domain.action.UriType.UriType
import domain.condition.Condition
import domain.fsm.FiniteStateMachine
import domain.guard.Guard
import domain.state.{State, StateType}
import domain.state.StateType.StateType
import domain.transition.Transition
import org.apache.jena.rdf.model.{Model, Resource}
import org.apache.jena.vocabulary.RDF

class JenaReader(properties: Properties) {
  def readFsm(model: Model, fsmUri: String, verbose: Boolean): FiniteStateMachine = {
    val fsmRes = model.getResource(fsmUri)

    //First retrieve the states
    var states: List[State] = List()
    fsmRes.listProperties(properties.Contains).forEachRemaining(property => {
      if (verbose) {
        println(property)
      }
      val resource = property.getResource
      if (resource.hasProperty(RDF.`type`, properties.StateClass)) {
        val state = getStateFromResource(resource)
        states = state :: states
      }
    })

    //Then retrieve the transitions
    var transitions: List[Transition] = List()
    fsmRes.listProperties(properties.Contains).forEachRemaining(property => {
      val resource = property.getResource
      if (resource.hasProperty(RDF.`type`, properties.TransitionClass)) {
        val transition = getTransitionFromResource(resource, states)
        transitions = transition :: transitions
      }
    })

    new FiniteStateMachine(name = fsmRes.getLocalName, states = states, transitions = transitions)
  }

  private def getTransitionFromResource(transitionRes: Resource, states: List[State]): Transition = {
    var sourceState = new State(0, 0)
    if (transitionRes.hasProperty(properties.HasSourceState)) {
      val sourceStateName = transitionRes.getProperty(properties.HasSourceState).getResource.getLocalName
      for (state <- states) {
        if (state.name.equals(sourceStateName)) sourceState = state
      }
    }

    var destinationState = new State(0, 0)
    if (transitionRes.hasProperty(properties.HasTargetState)) {
      val destinationStateName = transitionRes.getProperty(properties.HasTargetState).getResource.getLocalName
      for (state <- states) {
        if (state.name.equals(destinationStateName)) destinationState = state
      }
    }

    var guards: List[Guard] = List()
    transitionRes.listProperties(properties.HasTransitionGuard).mapWith(_.getResource).forEachRemaining(guardRes => {
      val guard = getGuardFromResource(guardRes)
      guards = guard :: guards
    })

    new Transition(name = transitionRes.getLocalName, source = sourceState, destination = destinationState, guards = guards)
  }

  private def getGuardFromResource(guardRes: Resource): Guard = {
    var actions: List[Action] = List()
    guardRes.listProperties(properties.HasGuardAction).mapWith(_.getResource).forEachRemaining(actionRes => {
      val action = getActionFromResource(actionRes, ActionType.GUARD)
      actions = action :: actions
    })

    var conditions: List[Condition] = List()
    guardRes.listProperties(properties.HasGuardCondition).mapWith(_.getResource).forEachRemaining(conditionRes => {
      val condition = getConditionFromResource(conditionRes)
      conditions = condition :: conditions
    })

    new Guard(name = guardRes.getLocalName, actions = actions, conditions = conditions)
  }

  private def getConditionFromResource(conditionRes: Resource): Condition = {
    var content = ""
    if (conditionRes.hasProperty(properties.HasContent)) {
      content = conditionRes.getProperty(properties.HasContent).getString
    }

    new Condition(name = conditionRes.getLocalName, _query = content)
  }

  private def getStateFromResource(stateRes: Resource): State = {
    var stateType: StateType = StateType.SIMPLE
    stateRes.listProperties(RDF.`type`).mapWith(_.getResource).forEachRemaining(classRes => {
      (stateType, getStateTypeFromClassResource(classRes)) match {
        case (StateType.SIMPLE, StateType.INITIAL) => stateType = StateType.INITIAL
        case (StateType.SIMPLE, StateType.FINAL) => stateType = StateType.FINAL
        case (StateType.INITIAL, StateType.FINAL) => stateType = StateType.INITIAL_FINAL
        case (StateType.FINAL, StateType.INITIAL) => stateType = StateType.INITIAL_FINAL
        case (_, _) =>
      }
    })

    var actions: List[Action] = List()
    stateRes.listProperties(properties.HasEntryAction).mapWith(_.getResource).forEachRemaining(actionRes => {
      actions = getActionFromResource(actionRes, ActionType.ENTRY) :: actions
    })
    stateRes.listProperties(properties.HasExitAction).mapWith(_.getResource).forEachRemaining(actionRes => {
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
    if (actionRes.hasProperty(properties.HasMethod)) {
      methodType = actionRes.getProperty(properties.HasMethod).getResource.getLocalName match {
        case "GET" => MethodType.GET
        case "POST" => MethodType.POST
        case _ => MethodType.GET
      }
    }

    var uriType: UriType = UriType.ABSOLUTE
    var absoluteUri: String = ""
    var prototypeUri: PrototypeUri = new PrototypeUri()
    if (actionRes.hasProperty(properties.HasAbsoluteUri)) {
      uriType = UriType.ABSOLUTE
      absoluteUri = actionRes.getProperty(properties.HasAbsoluteUri).getObject.toString

    } else if (actionRes.hasProperty(properties.HasPrototypeUri)) {
      uriType = UriType.PROTOTYPE
      val prototypeUriRes = actionRes.getProperty(properties.HasPrototypeUri).getResource
      prototypeUri = getPrototypeUriFromResource(prototypeUriRes)
    }

    var timeout: Int = 1000
    if (actionRes.hasProperty(properties.HasTimeoutInMs)) {
      timeout = actionRes.getProperty(properties.HasTimeoutInMs).getInt
    }

    var body: Body = new Body()
    if (actionRes.hasProperty(properties.HasBody)) {
      val bodyRes = actionRes.getProperty(properties.HasBody).getResource
      body = getBodyFromResource(bodyRes)
    }

    new Action(actionRes.getLocalName, actionType, methodType, body, absoluteUri, uriType, prototypeUri, timeout)
  }

  private def getPrototypeUriFromResource(prototypeUriRes: Resource): PrototypeUri = {
    var structure: String = ""
    if (prototypeUriRes.hasProperty(properties.HasStructure)) {
      structure = prototypeUriRes.getProperty(properties.HasStructure).getString
    }

    var parameters: List[PrototypeUriParameter] = List()
    prototypeUriRes.listProperties(properties.HasParameter).mapWith(_.getResource).forEachRemaining(parameterRes => {
      parameters = getPrototypeUriParameterFromResource(parameterRes) :: parameters
    })

    new PrototypeUri(name = prototypeUriRes.getLocalName, _structure = structure, prototypeUriParameters = parameters)
  }

  private def getPrototypeUriParameterFromResource(parameterRes: Resource): PrototypeUriParameter = {
    var placeholder: String = ""
    if (parameterRes.hasProperty(properties.HasPlaceholder)) {
      placeholder = parameterRes.getProperty(properties.HasPlaceholder).getString
    }

    var query: String = ""
    if (parameterRes.hasProperty(properties.HasQuery)) {
      query = parameterRes.getProperty(properties.HasQuery).getString
    }

    new PrototypeUriParameter(name = parameterRes.getLocalName, _placeholder = placeholder, _query = query)
  }

  private def getBodyFromResource(bodyRes: Resource): Body = {
    var bodyType: BodyType = BodyType.RDF
    if (bodyRes.hasProperty(properties.HasBodyType)) {
      bodyType = bodyRes.getProperty(properties.HasBodyType).getResource.getLocalName match {
        case "rdf" => BodyType.RDF
        case "executableSparql" => BodyType.SPARQL
        case "other" => BodyType.JSON
        case _ => BodyType.RDF
      }
    }

    var content = ""
    if (bodyRes.hasProperty(properties.HasBodyContent)) {
      content = bodyRes.getProperty(properties.HasBodyContent).getString
    }

    new Body(bodyRes.getLocalName, bodyType, content)
  }
}
