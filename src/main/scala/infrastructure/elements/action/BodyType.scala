package infrastructure.elements.action

import infrastructure.elements.action.UriType.Value

object BodyType extends Enumeration {
  type BodyType = Value
  val RDF, JSON, SPARQL = Value
}
