package infrastructure

import domain.environment.Environment
import infrastructure.jena.JenaFsmRepository

object EnvironmentSingleton {
  private val environment = new Environment(new JenaFsmRepository)

  def get(): Environment = environment
}
