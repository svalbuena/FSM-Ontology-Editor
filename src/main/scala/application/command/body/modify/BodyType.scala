package application.command.body.modify

object BodyType extends Enumeration {
  type BodyType = Value
  val RDF, JSON, SPARQL = Value
}
