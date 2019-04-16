package application.commandhandler.condition.modify

import application.command.condition.modify.ModifyConditionNameCommand
import domain.Environment

class ModifyConditionNameHandler {
  def execute(modifyConditionNameCommand: ModifyConditionNameCommand): Either[Exception, String] = {
    Environment.getCondition(modifyConditionNameCommand.conditionName) match {
      case Left(error) => Left(error)
      case Right(condition) => condition.name = modifyConditionNameCommand.newConditionName
    }
  }
}
