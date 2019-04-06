package infrastructure.elements.action.body

import infrastructure.elements.action.body.BodyType.BodyType
import infrastructure.propertybox.action.body.BodyPropertiesBox

class Body(var bodyType: BodyType, var content: String) {
  val propertiesBox = new BodyPropertiesBox(this)
}
