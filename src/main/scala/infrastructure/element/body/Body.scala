package infrastructure.element.body

import infrastructure.element.Element
import infrastructure.element.body.BodyType.BodyType
import infrastructure.propertybox.body.BodyPropertiesBox

class Body(name: String,
           var bodyType: BodyType = BodyType.RDF,
           var content: String = ""
          ) extends Element(name) {
  val propertiesBox = new BodyPropertiesBox()
}
