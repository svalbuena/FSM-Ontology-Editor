package infrastructure

import infrastructure.element.Element
import infrastructure.element.action.{Action, ActionType, MethodType, UriType}
import infrastructure.element.body.{Body, BodyType}
import infrastructure.element.condition.Condition
import infrastructure.element.fsm.FiniteStateMachine
import infrastructure.element.guard.Guard
import infrastructure.element.prototypeuri.PrototypeUri
import infrastructure.element.prototypeuriparameter.PrototypeUriParameter
import infrastructure.element.state.{State, StateType}
import infrastructure.element.transition.Transition

/**
  * Converter from an fsm model instance to an fsm infrastructure instance
  */
object DomainToInfrastructureConverter {

  /**
    *
    * @param domainFsm an fsm model instance
    * @return an fsm infrastructure instance
    */
  def convertFsm(domainFsm: domain.fsm.FiniteStateMachine): FiniteStateMachine = {
    val fsmName = domainFsm.name
    val fsmBaseUri = domainFsm.baseUri

    val fsm = new FiniteStateMachine(name = fsmName, baseUri = fsmBaseUri)

    for (domainState <- domainFsm.states) {
      val state = getState(domainState, fsm)
      fsm.addState(state)
    }

    for (domainTransition <- domainFsm.transitions) {
      val srcOpt = fsm.states.find(s => s.name.equals(domainTransition.source.name))
      val dstOpt = fsm.states.find(s => s.name.equals(domainTransition.destination.name))

      if (srcOpt.isDefined && dstOpt.isDefined) {
        val transition = getTransition(domainTransition, srcOpt.get, dstOpt.get, fsm)
        fsm.addTransition(transition)
      } else {
        println("Cant convert a transition, a state is not found")
      }
    }

    fsm
  }

  private def getState(domainState: domain.state.State, parent: FiniteStateMachine): State = {
    val stateName = domainState.name
    val x = domainState.x
    val y = domainState.y

    val stateType = domainState.stateType match {
      case domain.state.StateType.INITIAL => StateType.INITIAL
      case domain.state.StateType.SIMPLE => StateType.SIMPLE
      case domain.state.StateType.FINAL => StateType.FINAL
      case domain.state.StateType.INITIAL_FINAL => StateType.INITIAL_FINAL
    }

    val state = new State(name = stateName, x = x, y = y, stateType = stateType, parent = parent)

    for (domainAction <- domainState.actions) {
      val action = getAction(domainAction, state)
      state.addAction(action)
    }

    state
  }

  private def getAction(domainAction: domain.action.Action, parent: Element): Action = {
    val actionName = domainAction.name

    val actionType = domainAction.actionType match {
      case domain.action.ActionType.ENTRY => ActionType.ENTRY
      case domain.action.ActionType.EXIT => ActionType.EXIT
      case domain.action.ActionType.GUARD => ActionType.GUARD
    }

    val methodType = domainAction.methodType match {
      case domain.action.MethodType.GET => MethodType.GET
      case domain.action.MethodType.POST => MethodType.POST
    }

    val uriType = domainAction.uriType match {
      case domain.action.UriType.ABSOLUTE => UriType.ABSOLUTE
      case domain.action.UriType.PROTOTYPE => UriType.PROTOTYPE
    }

    val absoluteUri = domainAction.absoluteUri

    val prototypeUri = getPrototypeUri(domainAction.prototypeUri)

    val body = getBody(domainAction.body)

    val timeout = domainAction.timeout.toString

    new Action(name = actionName, actionType = actionType, method = methodType, body = body, uriType = uriType, absoluteUri = absoluteUri, prototypeUri = prototypeUri, timeout = timeout, parent = parent)
  }

  private def getPrototypeUri(domainPrototypeUri: domain.action.PrototypeUri): PrototypeUri = {
    val prototypeUriName = domainPrototypeUri.name

    val structure = domainPrototypeUri.structure

    val prototypeUri = new PrototypeUri(name = prototypeUriName, structure = structure)

    for (domainParameter <- domainPrototypeUri.prototypeUriParameters) {
      val parameter = getPrototypeUriParameter(domainParameter, prototypeUri)
      prototypeUri.addPrototypeUriParameter(parameter)
    }

    prototypeUri
  }

  private def getPrototypeUriParameter(domainParameter: domain.action.PrototypeUriParameter, parent: PrototypeUri): PrototypeUriParameter = {
    val parameterName = domainParameter.name

    val placeholder = domainParameter.placeholder

    val query = domainParameter.query

    new PrototypeUriParameter(name = parameterName, placeholder = placeholder, query = query, parent = parent)
  }

  private def getBody(domainBody: domain.action.Body): Body = {
    val bodyName = domainBody.name

    val bodyType = domainBody.bodyType match {
      case domain.action.BodyType.RDF => BodyType.RDF
      case domain.action.BodyType.JSON => BodyType.JSON
      case domain.action.BodyType.SPARQL => BodyType.SPARQL
    }

    val content = domainBody.content

    new Body(name = bodyName, bodyType = bodyType, content = content)
  }

  private def getTransition(domainTransition: domain.transition.Transition, srcState: State, dstState: State, parent: FiniteStateMachine): Transition = {
    val transitionName = domainTransition.name

    val transition = new Transition(name = transitionName, source = srcState, destination = dstState, isEditable = true, parent = parent)

    for (domainGuard <- domainTransition.guards) {
      val guard = getGuard(domainGuard, transition)
      transition.addGuard(guard)
    }

    srcState.outTransitions = transition :: srcState.outTransitions
    dstState.inTransitions = transition :: dstState.inTransitions

    transition
  }

  private def getGuard(domainGuard: domain.guard.Guard, parent: Transition): Guard = {
    val guardName = domainGuard.name

    val guard = new Guard(name = guardName, parent = parent)

    for (domainAction <- domainGuard.actions) {
      val action = getAction(domainAction, guard)
      guard.addAction(action)
    }

    for (domainCondition <- domainGuard.conditions) {
      val condition = getCondition(domainCondition, guard)
      guard.addCondition(condition)
    }

    guard
  }

  private def getCondition(domainCondition: domain.condition.Condition, parent: Guard): Condition = {
    val conditionName = domainCondition.name

    val query = domainCondition.query

    new Condition(name = conditionName, query = query, parent = parent)
  }
}
