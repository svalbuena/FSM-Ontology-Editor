package domain

import domain.fsm.FiniteStateMachine

trait FsmRepository {
  def saveFsm(fsm: FiniteStateMachine, filename: String): Either[Exception, _]
  def loadFsm(filename: String): Either[Exception, FiniteStateMachine]
}
