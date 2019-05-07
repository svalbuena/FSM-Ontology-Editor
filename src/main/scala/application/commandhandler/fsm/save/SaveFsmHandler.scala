package application.commandhandler.fsm.save

import application.command.fsm.save.SaveFsmCommand
import domain.Environment

class SaveFsmHandler(environment: Environment) {

  /**
    *
    * @param saveFsmCommand command
    * @return an exception or nothing if successful
    */
  def execute(saveFsmCommand: SaveFsmCommand): Either[Exception, _] = {
    environment.saveFsm(saveFsmCommand.filename)
  }
}
