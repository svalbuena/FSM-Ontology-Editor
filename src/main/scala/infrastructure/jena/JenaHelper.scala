package infrastructure.jena

import org.apache.jena.rdf.model.{Property, Resource, ResourceFactory}

object JenaHelper {
  def toJenaClass(string: String): Resource = ResourceFactory.createResource(string)

  def toJenaProperty(string: String): Property = ResourceFactory.createProperty(string)

  def toJenaResource(string: String): Resource = ResourceFactory.createResource(string)
}
