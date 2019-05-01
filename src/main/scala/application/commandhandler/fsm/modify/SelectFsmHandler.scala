package application.commandhandler.fsm.modify

import application.command.fsm.modify.SelectFsmCommand
import domain.Environment

class SelectFsmHandler {

  /**
    *
    * @param selectFsmCommand command
    * @return an exception or the selected fsm name
    */
  def execute(selectFsmCommand: SelectFsmCommand): Either[Exception, String] = {
    Environment.selectFsm(selectFsmCommand.fsmName)
  }
}
