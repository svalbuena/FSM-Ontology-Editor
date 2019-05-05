package domain.repository

class Properties(val fsmPrefix: String, val httpPrefix: String, val httpMethodsPrefix: String, val geometryPrefix: String) {

  val Contains: String = fsmPrefix + "contains"
  val HasEntryAction: String = fsmPrefix + "hasEntryAction"
  val HasExitAction: String = fsmPrefix + "hasExitAction"
  val HasBody: String = fsmPrefix + "hasBody"
  val HasPrototypeUri: String = fsmPrefix + "hasPrototypeURI"
  val HasBodyType: String = fsmPrefix + "hasBodyType"
  val HasBodyContent: String = fsmPrefix + "hasContent"
  val HasTimeoutInMs: String = fsmPrefix + "hasTimeoutInMs"
  val HasParameter: String = fsmPrefix + "hasParameter"
  val HasStructure: String = fsmPrefix + "hasStructure"
  val HasPlaceholder: String = fsmPrefix + "hasPlaceholder"
  val HasQuery: String = fsmPrefix + "hasQuery"
  val HasSourceState: String = fsmPrefix + "hasSourceState"
  val HasTargetState: String = fsmPrefix + "hasTargetState"
  val HasTransitionGuard: String = fsmPrefix + "hasTransitionGuard"
  val HasGuardCondition: String = fsmPrefix + "hasGuardCondition"
  val HasGuardAction: String = fsmPrefix + "hasGuardAction"
  val HasContent: String = fsmPrefix + "hasContent"


  val StateMachineClass: String = fsmPrefix + "StateMachine"
  val TransitionClass: String = fsmPrefix + "Transition"
  val StateClass: String = fsmPrefix + "State"
  val SimpleStateClass: String = fsmPrefix + "SimpleState"
  val InitialStateClass: String = fsmPrefix + "InitialState"
  val FinalStateClass: String = fsmPrefix + "FinalState"
  val BodyClass: String = fsmPrefix + "Body"
  val PrototypeUriClass: String = fsmPrefix + "PrototypeURI"
  val PrototypeUriParameterClass: String = fsmPrefix + "PrototypeParameter"
  val ConditionClass: String = fsmPrefix + "Condition"
  val ActionClass: String = fsmPrefix + "Action"
  val GuardClass: String = fsmPrefix + "Guard"

  val GetMethod: String = httpMethodsPrefix + "GET"
  val PostMethod: String = httpMethodsPrefix + "POST"

  val RdfBodyType: String = fsmPrefix + "rdf"
  val SparqlBodyType: String = fsmPrefix + "executableSparql"
  val OtherBodyType: String = fsmPrefix + "other"

  val HasMethod: String = httpPrefix + "mthd"
  val HasAbsoluteUri: String = httpPrefix + "absoluteURI"

  val lowerCorner: String = geometryPrefix + "lowerCorner"
  val coordX: String = geometryPrefix + "coordX"
  val coordY: String = geometryPrefix + "coordY"
  val pointClass: String = geometryPrefix + "Point"
}
