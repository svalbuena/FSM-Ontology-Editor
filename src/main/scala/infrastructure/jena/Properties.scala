package infrastructure.jena

import org.apache.jena.rdf.model.impl.PropertyImpl

class Properties(val fsmPrefix: String, val httpPrefix: String, val httpMethodsPrefix: String, val geometryPrefix: String) {

  val Contains = new PropertyImpl(fsmPrefix + "contains")
  val HasEntryAction = new PropertyImpl(fsmPrefix + "hasEntryAction")
  val HasExitAction = new PropertyImpl(fsmPrefix + "hasExitAction")
  val HasBody = new PropertyImpl(fsmPrefix + "hasBody")
  val HasPrototypeUri = new PropertyImpl(fsmPrefix + "hasPrototypeURI")
  val HasBodyType = new PropertyImpl(fsmPrefix + "hasBodyType")
  val HasBodyContent = new PropertyImpl(fsmPrefix + "hasContent")
  val HasTimeoutInMs = new PropertyImpl(fsmPrefix + "hasTimeoutInMs")
  val HasParameter = new PropertyImpl(fsmPrefix + "hasParameter")
  val HasStructure = new PropertyImpl(fsmPrefix + "hasStructure")
  val HasPlaceholder = new PropertyImpl(fsmPrefix + "hasPlaceholder")
  val HasQuery = new PropertyImpl(fsmPrefix + "hasQuery")
  val HasSourceState = new PropertyImpl(fsmPrefix + "hasSourceState")
  val HasTargetState = new PropertyImpl(fsmPrefix + "hasTargetState")
  val HasTransitionGuard = new PropertyImpl(fsmPrefix + "hasTransitionGuard")
  val HasGuardCondition = new PropertyImpl(fsmPrefix + "hasGuardCondition")
  val HasGuardAction = new PropertyImpl(fsmPrefix + "hasGuardAction")
  val HasContent = new PropertyImpl(fsmPrefix + "hasContent")


  val StateMachineClass = new PropertyImpl(fsmPrefix + "StateMachine")
  val TransitionClass = new PropertyImpl(fsmPrefix + "Transition")
  val StateClass = new PropertyImpl(fsmPrefix + "State")
  val SimpleStateClass = new PropertyImpl(fsmPrefix + "SimpleState")
  val InitialStateClass = new PropertyImpl(fsmPrefix + "InitialState")
  val FinalStateClass = new PropertyImpl(fsmPrefix + "FinalState")
  val BodyClass = new PropertyImpl(fsmPrefix + "Body")
  val PrototypeUriClass = new PropertyImpl(fsmPrefix + "PrototypeURI")
  val PrototypeUriParameterClass = new PropertyImpl(fsmPrefix + "PrototypeParameter")
  val ConditionClass = new PropertyImpl(fsmPrefix + "Condition")
  val ActionClass = new PropertyImpl(fsmPrefix + "Action")
  val GuardClass = new PropertyImpl(fsmPrefix + "Guard")

  val GetMethod = new PropertyImpl(httpMethodsPrefix + "GET")
  val PostMethod = new PropertyImpl(httpMethodsPrefix + "POST")

  val RdfBodyType = new PropertyImpl(fsmPrefix + "rdf")
  val SparqlBodyType = new PropertyImpl(fsmPrefix + "executableSparql")
  val OtherBodyType = new PropertyImpl(fsmPrefix + "other")

  val FsmClass = new PropertyImpl(fsmPrefix + "StateMachine")

  val HasMethod = new PropertyImpl(httpPrefix + "mthd")
  val HasAbsoluteUri = new PropertyImpl(httpPrefix + "absoluteURI")

  val lowerCorner = new PropertyImpl(geometryPrefix + "lowerCorner")
  val coordX = new PropertyImpl(geometryPrefix + "coordX")
  val coordY = new PropertyImpl(geometryPrefix + "coordY")
  val pointClass = new PropertyImpl(geometryPrefix + "Point")
  println(pointClass)
}
