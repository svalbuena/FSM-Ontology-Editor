package application.commandhandler.fsm.add

import application.command.fsm.add.LoadFsmCommand
import domain.Environment
import domain.fsm.FiniteStateMachine

class LoadFsmHandler {
  def execute(loadFsmCommand: LoadFsmCommand): Either[Exception, FiniteStateMachine] = {
    Environment.loadFsm(loadFsmCommand.filename)
  }
}
