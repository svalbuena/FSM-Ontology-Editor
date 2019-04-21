package infrastructure.jena

import domain.action.{Action, Body, PrototypeUri, PrototypeUriParameter}
import domain.condition.Condition
import domain.fsm.FiniteStateMachine
import domain.guard.Guard
import domain.state.State
import domain.transition.Transition
import org.apache.jena.rdf.model.{Model, ModelFactory, Resource}
import org.apache.jena.rdf.model.impl.{ResourceImpl, StatementImpl}
import org.apache.jena.vocabulary.RDF

class JenaWriter(properties: Properties, fsmBaseUri: String) {
  def writeFsm(fsm: FiniteStateMachine, filename: String): Model = {
    val fsmModel = ModelFactory.createDefaultModel()

    val fsmRes = fsmModel.createResource(fsmBaseUri + fsm.name)
    fsmRes.addProperty(RDF.`type`, properties.FsmClass)

    for (state <- fsm.states) {
      val stateRes = getStateResource(fsmModel, state)
      fsmRes.addProperty(properties.Contains, stateRes)
    }

    for (transition <- fsm.transitions) {
      val transitionRes = getTransitionResource(fsmModel, transition)

      fsmRes.addProperty(properties.Contains, transitionRes)
    }

    fsmModel
  }

  private def getStateResource(fsmModel: Model, state: State): Resource = {
    val stateRes = fsmModel.createResource(fsmBaseUri + state.name)
    state.stateType match {
      case domain.state.StateType.INITIAL =>
        stateRes.addProperty(RDF.`type`, properties.InitialStateClass)
      case domain.state.StateType.SIMPLE =>
        stateRes.addProperty(RDF.`type`, properties.SimpleStateClass)
      case domain.state.StateType.FINAL =>
        stateRes.addProperty(RDF.`type`, properties.FinalStateClass)
      case domain.state.StateType.INITIAL_FINAL =>
        stateRes.addProperty(RDF.`type`, properties.InitialStateClass)
        stateRes.addProperty(RDF.`type`, properties.FsmClass)
    }
    stateRes.addProperty(RDF.`type`, properties.StateClass)

    for (action <- state.actions) {
      val actionRes = getActionResource(fsmModel, action)

      action.actionType match {
        case domain.action.ActionType.ENTRY => stateRes.addProperty(properties.HasEntryAction, actionRes)
        case domain.action.ActionType.EXIT => stateRes.addProperty(properties.HasExitAction, actionRes)
        case _ =>
      }
    }

    stateRes
  }

  private def getActionResource(fsmModel: Model, action: Action): Resource = {
    val actionRes = fsmModel.createResource(fsmBaseUri + action.name)
    actionRes.addProperty(RDF.`type`, properties.ActionClass)

    val methodTypeProperty = action.methodType match {
      case domain.action.MethodType.GET => properties.GetMethod
      case domain.action.MethodType.POST => properties.PostMethod
    }
    actionRes.addProperty(properties.HasMethod, methodTypeProperty)

    action.uriType match {
      case domain.action.UriType.ABSOLUTE =>
        actionRes.addProperty(properties.HasAbsoluteUri, action.absoluteUri)
      case domain.action.UriType.PROTOTYPE =>
        val prototypeUriRes = getPrototypeUriResource(fsmModel, action.prototypeUri)
        actionRes.addProperty(properties.HasPrototypeUri, prototypeUriRes)
    }

    actionRes.addLiteral(properties.HasTimeoutInMs, action.timeout)

    val bodyRes = getBodyResource(fsmModel, action.body)
    actionRes.addProperty(properties.HasBody, bodyRes)

    actionRes
  }

  private def getBodyResource(fsmModel: Model, body: Body): Resource = {
    val bodyRes = fsmModel.createResource(fsmBaseUri + body.name)
    bodyRes.addProperty(RDF.`type`, properties.BodyClass)

    val bodyTypeProperty = body.bodyType match {
      case domain.action.BodyType.RDF => properties.RdfBodyType
      case domain.action.BodyType.JSON => properties.OtherBodyType
      case domain.action.BodyType.SPARQL => properties.SparqlBodyType
    }
    bodyRes.addProperty(properties.HasBodyType, bodyTypeProperty)

    bodyRes.addLiteral(properties.HasBodyContent, body.content)

    bodyRes
  }

  private def getPrototypeUriResource(fsmModel: Model, prototypeUri: PrototypeUri): Resource = {
    val prototypeUriRes = fsmModel.createResource(fsmBaseUri + prototypeUri.name)
    prototypeUriRes.addProperty(RDF.`type`, properties.PrototypeUriClass)

    prototypeUriRes.addLiteral(properties.HasStructure, prototypeUri.structure)

    for (parameter <- prototypeUri.prototypeUriParameters) {
      val parameterRes = getPrototypeUriParameterResource(fsmModel, parameter)

      prototypeUriRes.addProperty(properties.HasParameter, parameterRes)
    }

    prototypeUriRes
  }

  private def getPrototypeUriParameterResource(fsmModel: Model, parameter: PrototypeUriParameter): Resource = {
    val parameterRes = fsmModel.createResource(fsmBaseUri + parameter.name)
    parameterRes.addProperty(RDF.`type`, properties.PrototypeUriParameterClass)

    parameterRes.addLiteral(properties.HasPlaceholder, parameter.placeholder)

    parameterRes.addLiteral(properties.HasQuery, parameter.query)

    parameterRes
  }

  private def getTransitionResource(fsmModel: Model, transition: Transition): Resource = {
    val transitionRes = fsmModel.createResource(fsmBaseUri + transition.name)
    transitionRes.addProperty(RDF.`type`, properties.TransitionClass)

    transitionRes.addProperty(properties.HasSourceState, fsmModel.getResource(fsmBaseUri + transition.source.name))

    transitionRes.addProperty(properties.HasTargetState, fsmModel.getResource(fsmBaseUri + transition.destination.name))

    for (guard <- transition.guards) {
      val guardRes = getGuardResource(fsmModel, guard)

      transitionRes.addProperty(properties.HasTransitionGuard, guardRes)
    }

    transitionRes
  }

  private def getGuardResource(fsmModel: Model, guard: Guard): Resource = {
    val guardRes = fsmModel.createResource(fsmBaseUri + guard.name)
    guardRes.addProperty(RDF.`type`, properties.GuardClass)

    for (action <- guard.actions) {
      val actionRes = getActionResource(fsmModel, action)

      guardRes.addProperty(properties.HasGuardAction, actionRes)
    }

    for (condition <- guard.conditions) {
      val conditionRes = getConditionResource(fsmModel, condition)

      guardRes.addProperty(properties.HasGuardCondition, conditionRes)
    }

    guardRes
  }

  private def getConditionResource(fsmModel: Model, condition: Condition): Resource = {
    val conditionRes = fsmModel.createResource(fsmBaseUri + condition.name)
    conditionRes.addProperty(RDF.`type`, properties.ConditionClass)

    conditionRes.addLiteral(properties.HasContent, condition.query)

    conditionRes
  }
}
