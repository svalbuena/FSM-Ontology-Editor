package application.commandhandler.fsm.save

import application.command.fsm.save.SaveFsmCommand
import domain.Environment

class SaveFsmHandler {
  def execute(saveFsmCommand: SaveFsmCommand): Either[Exception, _] = {
    Environment.saveFsm(saveFsmCommand.filename)
  }
}
