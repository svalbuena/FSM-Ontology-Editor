package application.commandhandler.fsm.add

import application.command.fsm.add.AddFsmCommand
import domain.Environment

class AddFsmHandler {
  def handle(addFsmCommand: AddFsmCommand): String = {
    val fsm = Environment.addFsm()

    fsm.name
  }
}
