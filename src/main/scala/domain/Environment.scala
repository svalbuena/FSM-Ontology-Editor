package domain

import domain.fsm.FiniteStateMachine

object Environment {
  val nameList: List[String] = List()
  var fsmList: List[FiniteStateMachine] = List()

  def addFsm(): FiniteStateMachine = {
    var name = ""

    do {
      name = "fsm" + IdGenerator.getId

    } while (!nameList.contains(name))

    val fsm = new FiniteStateMachine(name)
    fsmList = fsm :: fsmList

    fsm
  }
}
