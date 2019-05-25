package application.commandhandler.fsm.modify

import application.command.fsm.modify.SelectFsmCommand
import domain.environment.Environment

class SelectFsmHandler(environment: Environment) {

  /**
    *
    * @param selectFsmCommand command
    * @return an exception or the selected fsm name
    */
  def execute(selectFsmCommand: SelectFsmCommand): Either[Exception, String] = {
    environment.selectFsm(selectFsmCommand.fsmName)
  }
}
