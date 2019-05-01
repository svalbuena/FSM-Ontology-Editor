package application.commandhandler.fsm.add

import application.command.fsm.add.AddFsmCommand
import domain.Environment
import domain.fsm.FiniteStateMachine

class AddFsmHandler {

  /**
    *
    * @param addFsmCommand command
    * @return an exception or the name of the fsm and the base uri
    */
  def execute(addFsmCommand: AddFsmCommand): Either[Exception, (String, String)] = {
    val fsm = new FiniteStateMachine

    Environment.addFsm(fsm) match {
      case Left(error) => Left(error)
      case Right(_) => Right(fsm.name, fsm.baseUri)
    }
  }
}
