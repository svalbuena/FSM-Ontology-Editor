package infrastructure.jena

import domain.action.ActionType.ActionType
import domain.action.BodyType.BodyType
import domain.action.MethodType.MethodType
import domain.action.UriType.UriType
import domain.action._
import domain.condition.Condition
import domain.fsm.FiniteStateMachine
import domain.guard.Guard
import domain.repository.Properties
import domain.state.StateType.StateType
import domain.state.{State, StateType}
import domain.transition.Transition
import org.apache.jena.rdf.model._
import org.apache.jena.util.iterator.ExtendedIterator
import org.apache.jena.vocabulary.RDF

import scala.language.implicitConversions

/**
  * Jena reader functionalities
  *
  * @param properties properties file with the properties and classes
  */
class JenaReader(properties: Properties) {

  /**
    * Reads the fsm from a model
    *
    * @param model model where the data has ben loaded
    * @return the infrastructure fsm instance
    */
  def readFsm(model: Model): Either[Exception, FiniteStateMachine] = {
    val fsmIterator = model.listResourcesWithProperty(RDF.`type`, JenaHelper.toJenaClass(properties.StateMachineClass))
    if (fsmIterator.hasNext) {
      val fsmRes = fsmIterator.next()

      val fsmName = fsmRes.getLocalName

      val fsmBaseUri = fsmRes.getNameSpace

      //First retrieve the states
      var states: List[State] = List()

      listResourceObjects(fsmRes, properties.contains).forEachRemaining(resource => {
        if (isResourceOfClass(resource, properties.StateClass)) {
          val state = getStateFromResource(resource)
          states = state :: states
        }
      })

      //Then retrieve the transitions
      var transitions: List[Transition] = List()
      listResourceObjects(fsmRes, properties.contains).forEachRemaining(resource => {
        if (isResourceOfClass(resource, properties.TransitionClass)) {
          var success = false

          val transitionRes = resource
          if (hasResourceProperty(transitionRes, properties.hasSourceState) && hasResourceProperty(transitionRes, properties.hasTargetState)) {
            val srcStateOpt = states.find(s => s.name.equals(getResourceProperty(transitionRes, properties.hasSourceState).getResource.getLocalName))
            val dstStateOpt = states.find(s => s.name.equals(getResourceProperty(transitionRes, properties.hasTargetState).getResource.getLocalName))

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
    listResourceObjects(transitionRes, properties.hasTransitionGuard).forEachRemaining(guardRes => {
      val guard = getGuardFromResource(guardRes)
      guards = guard :: guards
    })

    new Transition(name = transitionName, source = srcState, destination = dstState, guards = guards)
  }

  private def getGuardFromResource(guardRes: Resource): Guard = {
    val guardName = guardRes.getLocalName

    var actions: List[Action] = List()
    listResourceObjects(guardRes, properties.hasGuardAction).forEachRemaining(actionRes => {
      val action = getActionFromResource(actionRes, ActionType.GUARD)
      actions = action :: actions
    })

    var conditions: List[Condition] = List()
    listResourceObjects(guardRes, properties.hasGuardCondition).forEachRemaining(conditionRes => {
      val condition = getConditionFromResource(conditionRes)
      conditions = condition :: conditions
    })

    new Guard(name = guardName, actions = actions, conditions = conditions)
  }

  private def getConditionFromResource(conditionRes: Resource): Condition = {
    val conditionName = conditionRes.getLocalName

    val content = {
      if (hasResourceProperty(conditionRes, properties.hasQuery)) getResourceProperty(conditionRes, properties.hasQuery).getString
      else ""
    }

    new Condition(name = conditionName, _query = content)
  }

  private def getStateFromResource(stateRes: Resource): State = {
    val stateName = stateRes.getLocalName

    //paper http://www.eurecom.fr/~troncy/Publications/Troncy-lgd14.pdf
    val (x, y) = {
      if (hasResourceProperty(stateRes, properties.lowerCorner) && isResourceOfClass(getResourceProperty(stateRes, properties.lowerCorner).getResource, properties.PointClass)) {
        val lowerCornerRes = getResourceProperty(stateRes, properties.lowerCorner).getResource
        val x = {
          if (hasResourceProperty(lowerCornerRes, properties.coordinateX)) {
            getResourceProperty(lowerCornerRes, properties.coordinateX).getDouble
          } else {
            0.0
          }
        }

        val y = {
          if (hasResourceProperty(lowerCornerRes, properties.coordinateY)) {
            getResourceProperty(lowerCornerRes, properties.coordinateY).getDouble
          } else {
            0.0
          }
        }
        (x, y)
      } else {
        (0.0, 0.0)
      }
    }

    val stateType: StateType = {
      if (isResourceOfClass(stateRes, properties.InitialStateClass) && isResourceOfClass(stateRes, properties.FinalStateClass)) StateType.INITIAL_FINAL
      else if (isResourceOfClass(stateRes, properties.InitialStateClass)) StateType.INITIAL
      else if (isResourceOfClass(stateRes, properties.FinalStateClass)) StateType.FINAL
      else if (isResourceOfClass(stateRes, properties.SimpleStateClass)) StateType.SIMPLE
      else StateType.SIMPLE
    }

    var actions: List[Action] = List()
    listResourceObjects(stateRes, properties.hasEntryAction).forEachRemaining(actionRes => {
      actions = getActionFromResource(actionRes, ActionType.ENTRY) :: actions
    })
    listResourceObjects(stateRes, properties.hasExitAction).forEachRemaining(actionRes => {
      actions = getActionFromResource(actionRes, ActionType.EXIT) :: actions
    })

    new State(stateName, x, y, stateType, actions)
  }

  private def getActionFromResource(actionRes: Resource, actionType: ActionType): Action = {
    val actionName = actionRes.getLocalName

    val methodType: MethodType = {
      if (hasResourceProperty(actionRes, properties.hasMethod, properties.getMethod)) MethodType.GET
      else if (hasResourceProperty(actionRes, properties.hasMethod, properties.postMethod)) MethodType.POST
      else MethodType.GET
    }

    var uriType: UriType = UriType.ABSOLUTE
    var absoluteUri: String = ""
    var prototypeUri: PrototypeUri = new PrototypeUri()

    if (hasResourceProperty(actionRes, properties.hasAbsoluteUri)) {
      uriType = UriType.ABSOLUTE
      //TODO: mirar si se peude cambiar a getResource
      absoluteUri = getResourceProperty(actionRes, properties.hasAbsoluteUri).getObject.toString

    } else if (hasResourceProperty(actionRes, properties.hasPrototypeUri)) {
      uriType = UriType.PROTOTYPE
      val prototypeUriRes = getResourceProperty(actionRes, properties.hasPrototypeUri).getResource
      prototypeUri = getPrototypeUriFromResource(prototypeUriRes)
    }

    val timeout: Int = {
      if (hasResourceProperty(actionRes, properties.hasTimeoutInMs)) getResourceProperty(actionRes, properties.hasTimeoutInMs).getInt
      else 1000
    }

    val body: Body = {
      if (hasResourceProperty(actionRes, properties.hasBody)) getBodyFromResource(getResourceProperty(actionRes, properties.hasBody).getResource)
      else new Body()
    }

    new Action(actionName, actionType, methodType, body, absoluteUri, uriType, prototypeUri, timeout)
  }

  private def getPrototypeUriFromResource(prototypeUriRes: Resource): PrototypeUri = {
    val prototypeUriName = prototypeUriRes.getLocalName

    val structure: String = {
      if (hasResourceProperty(prototypeUriRes, properties.hasStructure)) getResourceProperty(prototypeUriRes, properties.hasStructure).getString
      else ""
    }


    var parameters: List[PrototypeUriParameter] = List()
    listResourceObjects(prototypeUriRes, properties.hasParameter).forEachRemaining(parameterRes => {
      parameters = getPrototypeUriParameterFromResource(parameterRes) :: parameters
    })

    new PrototypeUri(name = prototypeUriName, _structure = structure, prototypeUriParameters = parameters)
  }

  private def getPrototypeUriParameterFromResource(parameterRes: Resource): PrototypeUriParameter = {
    val parameterName = parameterRes.getLocalName

    val placeholder: String = {
      if (hasResourceProperty(parameterRes, properties.hasPlaceholder)) getResourceProperty(parameterRes, properties.hasPlaceholder).getString
      else ""
    }

    val query: String = {
      if (hasResourceProperty(parameterRes, properties.hasQuery)) getResourceProperty(parameterRes, properties.hasQuery).getString
      else ""
    }

    new PrototypeUriParameter(name = parameterName, _placeholder = placeholder, _query = query)
  }

  private def getBodyFromResource(bodyRes: Resource): Body = {
    val bodyName = bodyRes.getLocalName

    val bodyType: BodyType = {
      if (isResourceOfClass(bodyRes, properties.rdfBodyType)) BodyType.RDF
      else if (isResourceOfClass(bodyRes, properties.sparqlBodyType)) BodyType.SPARQL
      else if (isResourceOfClass(bodyRes, properties.otherBodyType)) BodyType.JSON
      else BodyType.RDF
    }

    val content = {
      if (hasResourceProperty(bodyRes, properties.hasContent)) getResourceProperty(bodyRes, properties.hasContent).getString
      else ""
    }

    new Body(bodyName, bodyType, content)
  }

  private def hasResourceProperty(resource: Resource, property: String): Boolean = {
    resource.hasProperty(JenaHelper.toJenaProperty(property))
  }

  private def hasResourceProperty(resource: Resource, property: String, objectS: String): Boolean = {
    resource.hasProperty(JenaHelper.toJenaProperty(property), JenaHelper.toJenaResource(objectS))
  }

  private def isResourceOfClass(resource: Resource, resClass: String): Boolean = {
    resource.hasProperty(RDF.`type`, JenaHelper.toJenaClass(resClass))
  }

  private def listResourceObjects(resource: Resource, property: String): ExtendedIterator[Resource] = {
    resource.listProperties(JenaHelper.toJenaProperty(property)).mapWith[Resource](_.getResource)
  }

  private def getResourceProperty(resource: Resource, property: String): Statement = {
    resource.getProperty(JenaHelper.toJenaProperty(property))
  }
}
