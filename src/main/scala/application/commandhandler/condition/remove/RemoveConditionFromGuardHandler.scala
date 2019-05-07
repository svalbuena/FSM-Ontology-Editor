package application.commandhandler.condition.remove

import application.command.condition.remove.RemoveConditionFromGuardCommand
import domain.Environment

class RemoveConditionFromGuardHandler(environment: Environment) {

  /**
    *
    * @param removeConditionFromGuardCommand command
    * @return an exception or nothing if successful
    */
  def execute(removeConditionFromGuardCommand: RemoveConditionFromGuardCommand): Either[Exception, _] = {
    environment.getCondition(removeConditionFromGuardCommand.conditionName) match {
      case Left(error) => Left(error)
      case Right(condition) =>
        environment.getGuard(removeConditionFromGuardCommand.guardName) match {
          case Left(error) => Left(error)
          case Right(guard) => guard.removeCondition(condition)
        }
    }
  }
}
