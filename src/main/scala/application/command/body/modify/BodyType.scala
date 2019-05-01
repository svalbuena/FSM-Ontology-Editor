package application.command.body.modify

/**
  * Enumeration of the body's types
  */
object BodyType extends Enumeration {
  type BodyType = Value
  val RDF, JSON, SPARQL = Value
}
