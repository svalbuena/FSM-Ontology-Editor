package domain

import domain.fsm.FiniteStateMachine
import infrastructure.jena.Properties

trait FsmRepository {
  def saveFsm(fsm: FiniteStateMachine, properties: Properties, filename: String): Either[Exception, _]
  def loadFsm(properties: Properties, filename: String): Either[Exception, FiniteStateMachine]
}
