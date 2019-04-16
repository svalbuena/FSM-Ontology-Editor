package application.commandhandler.condition.remove

import application.command.condition.remove.RemoveConditionFromGuardCommand
import domain.Environment

class RemoveConditionFromGuardHandler {
  def execute(removeConditionFromGuardCommand: RemoveConditionFromGuardCommand): Either[Exception, _] = {
    Environment.getCondition(removeConditionFromGuardCommand.conditionName) match {
      case Left(error) => Left(error)
      case Right(condition) =>
        Environment.getGuard(removeConditionFromGuardCommand.guardName) match {
          case Left(error) => Left(error)
          case Right(guard) => guard.removeCondition(condition)
        }
    }
  }
}
