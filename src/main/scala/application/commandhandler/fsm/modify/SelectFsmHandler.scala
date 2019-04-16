package application.commandhandler.fsm.modify

import application.command.fsm.modify.SelectFsmCommand
import domain.Environment

class SelectFsmHandler {
  def execute(selectFsmCommand: SelectFsmCommand): Either[Exception, String] = {
    Environment.selectFsm(selectFsmCommand.fsmName)
  }
}
