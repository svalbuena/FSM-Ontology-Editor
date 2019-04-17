package application.commandhandler.action.modify

import application.command.action.modify.ModifyActionMethodTypeCommand
import domain.Environment
import domain.action.MethodType

class ModifyActionMethodTypeHandler {
  def execute(modifyActionMethodCommand: ModifyActionMethodTypeCommand): Either[Exception, domain.action.MethodType.MethodType] = {
    Environment.getAction(modifyActionMethodCommand.actionName) match {
      case Left(error) => Left(error)
      case Right(action) => action.methodType = modifyActionMethodCommand.method match {
        case application.command.action.modify.MethodType.GET => MethodType.GET
        case application.command.action.modify.MethodType.POST => MethodType.POST
      }
    }
  }
}
