package infrastructure.jena

import domain.action.{Action, Body, PrototypeUri, PrototypeUriParameter}
import domain.condition.Condition
import domain.fsm.FiniteStateMachine
import domain.guard.Guard
import domain.repository.Properties
import domain.state.State
import domain.transition.Transition
import org.apache.jena.rdf.model.{Model, ModelFactory, Resource}
import org.apache.jena.vocabulary.{RDF, RDFS}

import scala.language.implicitConversions

/**
  * Jena writer functionalities
  *
  * @param properties properties file with the properties and classes
  * @param fsmBaseUri base uri to use
  */
class JenaWriter(properties: Properties, fsmBaseUri: String) {

  /**
    * Writes an fsm to a file
    *
    * @param fsm fsm infrastructure instance
    * @return the model where the fsm has been stored
    */
  def writeFsm(fsm: FiniteStateMachine): Model = {
    val fsmModel = ModelFactory.createDefaultModel()
    fsmModel.setNsPrefix("", fsmBaseUri)
    fsmModel.setNsPrefix("fsm", properties.fsmPrefix)
    fsmModel.setNsPrefix("http", properties.httpPrefix)
    fsmModel.setNsPrefix("http-methods", properties.httpMethodsPrefix)
    fsmModel.setNsPrefix("geom", properties.geometryPrefix)

    fsmModel.setNsPrefix("rdf", RDF.uri)
    fsmModel.setNsPrefix("rdfs", RDFS.uri)
    fsmModel.setNsPrefix("xml", "http://www.w3.org/2001/XMLSchema#")


    val fsmRes = fsmModel.createResource(fsmBaseUri + fsm.name)
    fsmRes.addProperty(RDF.`type`, JenaHelper.toJenaClass(properties.StateMachineClass))

    for (state <- fsm.states) {
      val stateRes = getStateResource(fsmModel, state)
      fsmRes.addProperty(JenaHelper.toJenaProperty(properties.Contains), stateRes)
    }

    for (transition <- fsm.transitions) {
      val transitionRes = getTransitionResource(fsmModel, transition)
      fsmRes.addProperty(JenaHelper.toJenaProperty(properties.Contains), transitionRes)
    }

    fsmModel
  }

  private def getStateResource(fsmModel: Model, state: State): Resource = {
    val stateRes = fsmModel.createResource(fsmBaseUri + state.name)

    val pointRes = fsmModel.createResource(fsmBaseUri + state.name + "Point")
    pointRes.addProperty(RDF.`type`, JenaHelper.toJenaClass(properties.pointClass))
    pointRes.addLiteral(JenaHelper.toJenaProperty(properties.coordX), state.x)
    pointRes.addLiteral(JenaHelper.toJenaProperty(properties.coordY), state.y)

    stateRes.addProperty(JenaHelper.toJenaProperty(properties.lowerCorner), pointRes)

    state.stateType match {
      case domain.state.StateType.INITIAL =>
        stateRes.addProperty(RDF.`type`, JenaHelper.toJenaClass(properties.InitialStateClass))
      case domain.state.StateType.SIMPLE =>
        stateRes.addProperty(RDF.`type`, JenaHelper.toJenaClass(properties.SimpleStateClass))
      case domain.state.StateType.FINAL =>
        stateRes.addProperty(RDF.`type`, JenaHelper.toJenaClass(properties.FinalStateClass))
      case domain.state.StateType.INITIAL_FINAL =>
        stateRes.addProperty(RDF.`type`, JenaHelper.toJenaClass(properties.InitialStateClass))
        stateRes.addProperty(RDF.`type`, JenaHelper.toJenaClass(properties.FinalStateClass))
    }
    stateRes.addProperty(RDF.`type`, JenaHelper.toJenaClass(properties.StateClass))

    for (action <- state.actions) {
      val actionRes = getActionResource(fsmModel, action)

      action.actionType match {
        case domain.action.ActionType.ENTRY => stateRes.addProperty(JenaHelper.toJenaProperty(properties.HasEntryAction), actionRes)
        case domain.action.ActionType.EXIT => stateRes.addProperty(JenaHelper.toJenaProperty(properties.HasExitAction), actionRes)
        case _ =>
      }
    }

    stateRes
  }

  private def getActionResource(fsmModel: Model, action: Action): Resource = {
    val actionRes = fsmModel.createResource(fsmBaseUri + action.name)
    actionRes.addProperty(RDF.`type`, JenaHelper.toJenaClass(properties.ActionClass))

    val methodTypeProperty = action.methodType match {
      case domain.action.MethodType.GET => properties.GetMethod
      case domain.action.MethodType.POST => properties.PostMethod
    }
    actionRes.addProperty(JenaHelper.toJenaProperty(properties.HasMethod), JenaHelper.toJenaResource(methodTypeProperty))

    action.uriType match {
      case domain.action.UriType.ABSOLUTE =>
        //TODO: mirar absolutURI
        actionRes.addProperty(JenaHelper.toJenaProperty(properties.HasAbsoluteUri), action.absoluteUri)
      case domain.action.UriType.PROTOTYPE =>
        val prototypeUriRes = getPrototypeUriResource(fsmModel, action.prototypeUri)
        actionRes.addProperty(JenaHelper.toJenaProperty(properties.HasPrototypeUri), prototypeUriRes)
    }

    actionRes.addLiteral(JenaHelper.toJenaProperty(properties.HasTimeoutInMs), action.timeout)

    val bodyRes = getBodyResource(fsmModel, action.body)
    actionRes.addProperty(JenaHelper.toJenaProperty(properties.HasBody), bodyRes)

    actionRes
  }

  private def getBodyResource(fsmModel: Model, body: Body): Resource = {
    val bodyRes = fsmModel.createResource(fsmBaseUri + body.name)
    bodyRes.addProperty(RDF.`type`, JenaHelper.toJenaClass(properties.BodyClass))

    val bodyTypeResource = body.bodyType match {
      case domain.action.BodyType.RDF => properties.RdfBodyType
      case domain.action.BodyType.JSON => properties.OtherBodyType
      case domain.action.BodyType.SPARQL => properties.SparqlBodyType
    }
    bodyRes.addProperty(JenaHelper.toJenaProperty(properties.HasBodyType), JenaHelper.toJenaResource(bodyTypeResource))

    bodyRes.addLiteral(JenaHelper.toJenaProperty(properties.HasBodyContent), body.content)

    bodyRes
  }

  private def getPrototypeUriResource(fsmModel: Model, prototypeUri: PrototypeUri): Resource = {
    val prototypeUriRes = fsmModel.createResource(fsmBaseUri + prototypeUri.name)
    prototypeUriRes.addProperty(RDF.`type`, JenaHelper.toJenaClass(properties.PrototypeUriClass))

    prototypeUriRes.addLiteral(JenaHelper.toJenaProperty(properties.HasStructure), prototypeUri.structure)

    for (parameter <- prototypeUri.prototypeUriParameters) {
      val parameterRes = getPrototypeUriParameterResource(fsmModel, parameter)

      prototypeUriRes.addProperty(JenaHelper.toJenaProperty(properties.HasParameter), parameterRes)
    }

    prototypeUriRes
  }

  private def getPrototypeUriParameterResource(fsmModel: Model, parameter: PrototypeUriParameter): Resource = {
    val parameterRes = fsmModel.createResource(fsmBaseUri + parameter.name)
    parameterRes.addProperty(RDF.`type`, JenaHelper.toJenaClass(properties.PrototypeUriParameterClass))

    parameterRes.addLiteral(JenaHelper.toJenaProperty(properties.HasPlaceholder), parameter.placeholder)

    parameterRes.addLiteral(JenaHelper.toJenaProperty(properties.HasQuery), parameter.query)

    parameterRes
  }

  private def getTransitionResource(fsmModel: Model, transition: Transition): Resource = {
    val transitionRes = fsmModel.createResource(fsmBaseUri + transition.name)
    transitionRes.addProperty(RDF.`type`, JenaHelper.toJenaClass(properties.TransitionClass))

    transitionRes.addProperty(JenaHelper.toJenaProperty(properties.HasSourceState), fsmModel.getResource(fsmBaseUri + transition.source.name))

    transitionRes.addProperty(JenaHelper.toJenaProperty(properties.HasTargetState), fsmModel.getResource(fsmBaseUri + transition.destination.name))

    for (guard <- transition.guards) {
      val guardRes = getGuardResource(fsmModel, guard)

      transitionRes.addProperty(JenaHelper.toJenaProperty(properties.HasTransitionGuard), guardRes)
    }

    transitionRes
  }

  private def getGuardResource(fsmModel: Model, guard: Guard): Resource = {
    val guardRes = fsmModel.createResource(fsmBaseUri + guard.name)
    guardRes.addProperty(RDF.`type`, JenaHelper.toJenaClass(properties.GuardClass))

    for (action <- guard.actions) {
      val actionRes = getActionResource(fsmModel, action)

      guardRes.addProperty(JenaHelper.toJenaProperty(properties.HasGuardAction), actionRes)
    }

    for (condition <- guard.conditions) {
      val conditionRes = getConditionResource(fsmModel, condition)

      guardRes.addProperty(JenaHelper.toJenaProperty(properties.HasGuardCondition), conditionRes)
    }

    guardRes
  }

  private def getConditionResource(fsmModel: Model, condition: Condition): Resource = {
    val conditionRes = fsmModel.createResource(fsmBaseUri + condition.name)
    conditionRes.addProperty(RDF.`type`, JenaHelper.toJenaClass(properties.ConditionClass))

    conditionRes.addLiteral(JenaHelper.toJenaProperty(properties.HasContent), condition.query)

    conditionRes
  }
}
