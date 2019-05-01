package application.command.body.modify

import application.command.body.modify.BodyType.BodyType

/**
  *
  * @param bodyName name of the body
  * @param bodyType new type of the body
  */
class ModifyBodyTypeCommand(val bodyName: String, val bodyType: BodyType) {

}
