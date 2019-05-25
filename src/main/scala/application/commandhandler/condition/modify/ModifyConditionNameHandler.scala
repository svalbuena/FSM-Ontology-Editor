package application.commandhandler.condition.modify

import application.command.condition.modify.ModifyConditionNameCommand
import domain.environment.Environment

class ModifyConditionNameHandler(environment: Environment) {

  /**
    *
    * @param modifyConditionNameCommand command
    * @return an exception or the new condition name
    */
  def execute(modifyConditionNameCommand: ModifyConditionNameCommand): Either[Exception, String] = {
    environment.getCondition(modifyConditionNameCommand.conditionName) match {
      case Left(error) => Left(error)
      case Right(condition) => condition.name = modifyConditionNameCommand.newConditionName
    }
  }
}
