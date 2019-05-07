package application.commandhandler.condition.add

import application.command.condition.add.AddConditionToGuardCommand
import domain.Environment
import domain.condition.Condition

class AddConditionToGuardHandler(environment: Environment) {

  /**
    *
    * @param addConditionToGuardCommand command
    * @return an exception or the condition name
    */
  def execute(addConditionToGuardCommand: AddConditionToGuardCommand): Either[Exception, String] = {
    environment.getGuard(addConditionToGuardCommand.guardName) match {
      case Left(error) => Left(error)
      case Right(guard) =>
        val condition = new Condition(environment)
        guard.addCondition(condition) match {
          case Left(error) => Left(error)
          case Right(_) => Right(condition.name)
        }
    }
  }
}
