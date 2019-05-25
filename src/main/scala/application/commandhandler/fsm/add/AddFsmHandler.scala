package application.commandhandler.fsm.add

import application.command.fsm.add.AddFsmCommand
import domain.element.fsm.FiniteStateMachine
import domain.environment.Environment

class AddFsmHandler(environment: Environment) {

  /**
    *
    * @param addFsmCommand command
    * @return an exception or the name of the fsm and the base uri
    */
  def execute(addFsmCommand: AddFsmCommand): Either[Exception, (String, String)] = {
    val fsm = new FiniteStateMachine(environment)

    environment.addFsm(fsm) match {
      case Left(error) => Left(error)
      case Right(_) => Right(fsm.name, fsm.baseUri)
    }
  }
}
