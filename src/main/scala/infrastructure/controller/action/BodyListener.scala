package infrastructure.controller.action

import infrastructure.elements.action.body.Body

class BodyListener(body: Body) {
  private val propertiesBox = body.propertiesBox

  propertiesBox.setOnBodyTypeChanged(bodyType => {
    //TODO: notify the model
    body.bodyType = bodyType

    println("Body type changed to -> " + bodyType)
  })

  propertiesBox.setOnBodyContentChanged(content => {
    //TODO: notify the model
    body.content = content

    println("Body content changed to -> " + content)
  })
}
