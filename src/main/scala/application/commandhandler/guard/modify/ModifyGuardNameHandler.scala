package application.commandhandler.guard.modify

import application.command.guard.modify.ModifyGuardNameCommand
import domain.Environment

class ModifyGuardNameHandler(environment: Environment) {

  /**
    *
    * @param modifyGuardNameCommand command
    * @return an exception or guard name
    */
  def execute(modifyGuardNameCommand: ModifyGuardNameCommand): Either[Exception, String] = {
    environment.getGuard(modifyGuardNameCommand.guardName) match {
      case Left(error) => Left(error)
      case Right(guard) => guard.name = modifyGuardNameCommand.newGuardName
    }
  }
}
