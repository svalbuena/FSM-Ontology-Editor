package infrastructure.jena

import domain.fsm.FiniteStateMachine
import org.apache.jena.rdf.model.{Model, ModelFactory}
import org.apache.jena.rdf.model.impl.{ResourceImpl, StatementImpl}
import org.apache.jena.vocabulary.RDF

class JenaWriter(properties: Properties) {
  def writeFsm(fsm: FiniteStateMachine, fsmBaseUri: String, filename: String): Model = {
    val fsmModel = ModelFactory.createDefaultModel()

    val fsmRes = fsmModel.createResource(fsmBaseUri + fsm.name)
    fsmRes.addProperty(RDF.`type`, properties.FsmClass)

    for (state <- fsm.states) {
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

      fsmRes.addProperty(properties.Contains, stateRes)
    }

    for (transition <- fsm.transitions) {
      val transitionRes = fsmModel.createResource(fsmBaseUri + transition.name)
      transitionRes.addProperty(RDF.`type`, properties.TransitionClass)
      transitionRes.addProperty(properties.HasSourceState, fsmModel.getResource(fsmBaseUri + transition.source.name))
      transitionRes.addProperty(properties.HasTargetState, fsmModel.getResource(fsmBaseUri + transition.destination.name))

      fsmRes.addProperty(properties.Contains, transitionRes)
    }

    fsmModel
  }
}
