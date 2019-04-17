package infrastructure.controller.body

import infrastructure.elements.body.Body

class BodyController(body: Body) {
  private val propertiesBox = body.propertiesBox

  propertiesBox.setOnBodyTypeChanged(bodyType => {
    //TODO: notify the model, ModifyBodyType
    body.bodyType = bodyType

    println("Body type changed to -> " + bodyType)
  })

  propertiesBox.setOnBodyContentChanged(content => {
    //TODO: notify the model, ModifyBodyContent
    body.content = content

    println("Body content changed to -> " + content)
  })
}

object BodyController {
  def modifyBodyContent(): Unit = {

  }

  def modifyBodyName(): Unit = {

  }

  def modifyBodyType(): Unit = {

  }
}
