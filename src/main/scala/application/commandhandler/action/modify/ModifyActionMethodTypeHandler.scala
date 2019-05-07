package application.commandhandler.action.modify

import application.command.action.modify.ModifyActionMethodTypeCommand
import domain.Environment
import domain.action.MethodType

class ModifyActionMethodTypeHandler(environment: Environment) {

  /**
    *
    * @param modifyActionMethodCommand command
    * @return an exception or the method type
    */
  def execute(modifyActionMethodCommand: ModifyActionMethodTypeCommand): Either[Exception, domain.action.MethodType.MethodType] = {
    environment.getAction(modifyActionMethodCommand.actionName) match {
      case Left(error) => Left(error)
      case Right(action) => action.methodType = modifyActionMethodCommand.method match {
        case application.command.action.modify.MethodType.GET => MethodType.GET
        case application.command.action.modify.MethodType.POST => MethodType.POST
      }
    }
  }
}
