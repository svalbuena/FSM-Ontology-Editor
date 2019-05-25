package application.commandhandler.condition.modify

import application.command.condition.modify.ModifyConditionQueryCommand
import domain.environment.Environment

class ModifyConditionQueryHandler(environment: Environment) {

  /**
    *
    * @param modifyConditionQueryCommand command
    * @return an exception or the query
    */
  def execute(modifyConditionQueryCommand: ModifyConditionQueryCommand): Either[Exception, String] = {
    environment.getCondition(modifyConditionQueryCommand.conditionName) match {
      case Left(error) => Left(error)
      case Right(condition) => condition.query = modifyConditionQueryCommand.query
    }
  }
}
