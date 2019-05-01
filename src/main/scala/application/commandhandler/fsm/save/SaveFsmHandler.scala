package application.commandhandler.fsm.save

import application.command.fsm.save.SaveFsmCommand
import domain.Environment

class SaveFsmHandler {

  /**
    *
    * @param saveFsmCommand command
    * @return an exception or nothing if successful
    */
  def execute(saveFsmCommand: SaveFsmCommand): Either[Exception, _] = {
    Environment.saveFsm(saveFsmCommand.filename)
  }
}
