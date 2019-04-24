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
  def readFsm(model: Model): Either[Exception, FiniteStateMachine] = {
    val fsmIterator = model.listResourcesWithProperty(RDF.`type`, properties.StateMachineClass)
    if (fsmIterator.hasNext) {
      val fsmRes = fsmIterator.next()

      val fsmName = fsmRes.getLocalName

      val fsmBaseUri = fsmRes.getNameSpace

      //First retrieve the states
      var states: List[State] = List()
      fsmRes.listProperties(properties.Contains).forEachRemaining(property => {
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
          var success = false

          val transitionRes = resource
          if (transitionRes.hasProperty(properties.HasSourceState) && transitionRes.hasProperty(properties.HasTargetState)) {
            val srcStateOpt = states.find(s => s.name.equals(transitionRes.getProperty(properties.HasSourceState).getResource.getLocalName))
            val dstStateOpt = states.find(s => s.name.equals(transitionRes.getProperty(properties.HasTargetState).getResource.getLocalName))

            if (srcStateOpt.isDefined && dstStateOpt.isDefined) {
              val transition = getTransitionFromResource(transitionRes, srcStateOpt.get, dstStateOpt.get)
              transitions = transition :: transitions
              success = true
            }
          }
          if (!success) {
            println("Cannot find the states of the transition or the transition")
          }
        }
      })

      val fsm = new FiniteStateMachine(name = fsmName, _baseUri = fsmBaseUri, states = states, transitions = transitions)
      Right(fsm)
    } else {
      Left(new Exception("Fsm not found"))
    }
  }

  private def getTransitionFromResource(transitionRes: Resource, srcState: State, dstState: State): Transition = {
    val transitionName = transitionRes.getLocalName

    var guards: List[Guard] = List()
    transitionRes.listProperties(properties.HasTransitionGuard).mapWith(_.getResource).forEachRemaining(guardRes => {
      val guard = getGuardFromResource(guardRes)
      guards = guard :: guards
    })

    new Transition(name = transitionName, source = srcState, destination = dstState, guards = guards)
  }

  private def getGuardFromResource(guardRes: Resource): Guard = {
    val guardName = guardRes.getLocalName

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

    new Guard(name = guardName, actions = actions, conditions = conditions)
  }

  private def getConditionFromResource(conditionRes: Resource): Condition = {
    val conditionName = conditionRes.getLocalName

    val content = {
      if (conditionRes.hasProperty(properties.HasContent)) conditionRes.getProperty(properties.HasContent).getString
      else ""
    }

    new Condition(name = conditionName, _query = content)
  }

  private def getStateFromResource(stateRes: Resource): State = {
    val stateName = stateRes.getLocalName

    val stateType: StateType = {
      if (stateRes.hasProperty(RDF.`type`, properties.InitialStateClass) && stateRes.hasProperty(RDF.`type`, properties.FinalStateClass)) StateType.INITIAL_FINAL
      else if (stateRes.hasProperty(RDF.`type`, properties.InitialStateClass)) StateType.INITIAL
      else if (stateRes.hasProperty(RDF.`type`, properties.FinalStateClass)) StateType.FINAL
      else if (stateRes.hasProperty(RDF.`type`, properties.SimpleStateClass)) StateType.SIMPLE
      else StateType.SIMPLE
    }

    var actions: List[Action] = List()
    stateRes.listProperties(properties.HasEntryAction).mapWith(_.getResource).forEachRemaining(actionRes => {
      actions = getActionFromResource(actionRes, ActionType.ENTRY) :: actions
    })
    stateRes.listProperties(properties.HasExitAction).mapWith(_.getResource).forEachRemaining(actionRes => {
      actions = getActionFromResource(actionRes, ActionType.EXIT) :: actions
    })

    new State(stateName, 0, 0, stateType, actions)
  }

  private def getActionFromResource(actionRes: Resource, actionType: ActionType): Action = {
    val actionName = actionRes.getLocalName

    val methodType: MethodType = {
      if (actionRes.hasProperty(properties.HasMethod, properties.GetMethod)) MethodType.GET
      else if (actionRes.hasProperty(properties.HasMethod, properties.PostMethod)) MethodType.POST
      else MethodType.GET
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

    val timeout: Int = {
      if (actionRes.hasProperty(properties.HasTimeoutInMs)) actionRes.getProperty(properties.HasTimeoutInMs).getInt
      else 1000
    }

    val body: Body = {
      if (actionRes.hasProperty(properties.HasBody)) getBodyFromResource(actionRes.getProperty(properties.HasBody).getResource)
      else new Body()
    }

    new Action(actionName, actionType, methodType, body, absoluteUri, uriType, prototypeUri, timeout)
  }

  private def getPrototypeUriFromResource(prototypeUriRes: Resource): PrototypeUri = {
    val prototypeUriName = prototypeUriRes.getLocalName

    val structure: String = {
      if (prototypeUriRes.hasProperty(properties.HasStructure)) prototypeUriRes.getProperty(properties.HasStructure).getString
      else ""
    }


    var parameters: List[PrototypeUriParameter] = List()
    prototypeUriRes.listProperties(properties.HasParameter).mapWith(_.getResource).forEachRemaining(parameterRes => {
      parameters = getPrototypeUriParameterFromResource(parameterRes) :: parameters
    })

    new PrototypeUri(name = prototypeUriName, _structure = structure, prototypeUriParameters = parameters)
  }

  private def getPrototypeUriParameterFromResource(parameterRes: Resource): PrototypeUriParameter = {
    val parameterName = parameterRes.getLocalName

    val placeholder: String = {
      if (parameterRes.hasProperty(properties.HasPlaceholder)) parameterRes.getProperty(properties.HasPlaceholder).getString
      else ""
    }


    val query: String = {
      if (parameterRes.hasProperty(properties.HasQuery)) parameterRes.getProperty(properties.HasQuery).getString
      else ""
    }

    new PrototypeUriParameter(name = parameterName, _placeholder = placeholder, _query = query)
  }

  private def getBodyFromResource(bodyRes: Resource): Body = {
    val bodyName = bodyRes.getLocalName

    val bodyType: BodyType = {
      if (bodyRes.hasProperty(properties.HasBodyType, properties.RdfBodyType)) BodyType.RDF
      else if (bodyRes.hasProperty(properties.HasBodyType, properties.SparqlBodyType)) BodyType.SPARQL
      else if (bodyRes.hasProperty(properties.HasBodyType, properties.OtherBodyType)) BodyType.JSON
      else BodyType.RDF
    }

    val content = {
      if (bodyRes.hasProperty(properties.HasBodyContent)) bodyRes.getProperty(properties.HasBodyContent).getString
      else ""
    }

    new Body(bodyName, bodyType, content)
  }
}
