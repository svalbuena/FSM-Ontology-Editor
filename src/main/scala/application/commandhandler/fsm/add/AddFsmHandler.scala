package application.commandhandler.fsm.add

import application.command.fsm.add.AddFsmCommand
import domain.Environment
import domain.fsm.FiniteStateMachine

class AddFsmHandler {
  def execute(addFsmCommand: AddFsmCommand): Either[Exception, String] = {
    val fsm = new FiniteStateMachine

    Environment.addFsm(fsm) match {
      case Left(error) => Left(error)
      case Right(_) => Right(fsm.name)
    }
  }
}
