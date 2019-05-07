package application.commandhandler.fsm.add

import application.command.fsm.add.LoadFsmCommand
import domain.Environment
import domain.fsm.FiniteStateMachine

class LoadFsmHandler(environment: Environment) {

  /**
    *
    * @param loadFsmCommand command
    * @return an exception or the finite state machine as a domain instance
    */
  def execute(loadFsmCommand: LoadFsmCommand): Either[Exception, FiniteStateMachine] = {
    environment.loadFsm(loadFsmCommand.filename)
  }
}
