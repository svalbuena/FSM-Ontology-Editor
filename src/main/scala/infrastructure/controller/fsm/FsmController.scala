package infrastructure.controller.fsm

import application.command.fsm.add.AddFsmCommand
import application.command.fsm.modify.ModifyFsmNameCommand
import application.command.fsm.remove.RemoveFsmCommand
import application.commandhandler.fsm.add.AddFsmHandler
import application.commandhandler.fsm.modify.ModifyFsmNameHandler
import application.commandhandler.fsm.remove.RemoveFsmHandler

class FsmController {

}

object FsmController {
  def addFsm(): Unit = {
    new AddFsmHandler().execute(new AddFsmCommand) match {
      case Left(error) => println(error.getMessage)
      case Right(fsmName) =>
      //TODO: implement AddFsm
    }
  }

  def modifyFsmName(newFsmName: String): Unit = {
    new ModifyFsmNameHandler().execute(new ModifyFsmNameCommand(newFsmName)) match {
      case Left(error) => println(error.getMessage)
      case Right(_) =>
      //TODO: implement ModifyFsmName
    }
  }

  def removeFsm(fsmName: String): Unit = {
    new RemoveFsmHandler().execute(new RemoveFsmCommand(fsmName)) match {
      case Left(error) => println(error.getMessage)
      case Right(_) =>
      //TODO: implement RemoveFsm
    }
  }
}
