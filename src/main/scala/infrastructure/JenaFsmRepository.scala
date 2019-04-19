package infrastructure

import domain.FsmRepository
import domain.fsm.FiniteStateMachine

class JenaFsmRepository extends FsmRepository {
  override def saveFsm(fsm: FiniteStateMachine, filename: String): Either[Exception, _] = {
    Right(())
  }

  override def loadFsm(filename: String): Either[Exception, FiniteStateMachine] = {
    val fsm = new FiniteStateMachine()

    Right(fsm)
  }
}
