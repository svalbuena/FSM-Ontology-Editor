package domain.repository

class Properties(val fsmPrefix: String, val httpPrefix: String, val httpMethodsPrefix: String, val geometryPrefix: String) {

  val contains: String = fsmPrefix + "contains"
  val hasEntryAction: String = fsmPrefix + "hasEntryAction"
  val hasExitAction: String = fsmPrefix + "hasExitAction"
  val hasBody: String = fsmPrefix + "hasBody"
  val hasPrototypeUri: String = fsmPrefix + "hasPrototypeURI"
  val hasBodyType: String = fsmPrefix + "hasBodyType"
  val hasTimeoutInMs: String = fsmPrefix + "hasTimeoutInMs"
  val hasParameter: String = fsmPrefix + "hasParameter"
  val hasStructure: String = fsmPrefix + "hasStructure"
  val hasPlaceholder: String = fsmPrefix + "hasPlaceholder"
  val hasQuery: String = fsmPrefix + "hasQuery"
  val hasSourceState: String = fsmPrefix + "hasSourceState"
  val hasTargetState: String = fsmPrefix + "hasTargetState"
  val hasTransitionGuard: String = fsmPrefix + "hasTransitionGuard"
  val hasGuardCondition: String = fsmPrefix + "hasGuardCondition"
  val hasGuardAction: String = fsmPrefix + "hasGuardAction"
  val hasContent: String = fsmPrefix + "hasContent"

  val rdfBodyType: String = fsmPrefix + "rdf"
  val sparqlBodyType: String = fsmPrefix + "executableSparql"
  val otherBodyType: String = fsmPrefix + "other"

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


  val hasMethod: String = httpPrefix + "mthd"
  val hasAbsoluteUri: String = httpPrefix + "absoluteURI"

  val RequestClass: String = httpPrefix + "Request"


  val getMethod: String = httpMethodsPrefix + "GET"
  val postMethod: String = httpMethodsPrefix + "POST"


  val lowerCorner: String = geometryPrefix + "lowerCorner"
  val coordinateX: String = geometryPrefix + "coordX"
  val coordinateY: String = geometryPrefix + "coordY"
  val PointClass: String = geometryPrefix + "Point"
}
