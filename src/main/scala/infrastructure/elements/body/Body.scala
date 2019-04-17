package infrastructure.elements.body

import infrastructure.elements.Element
import infrastructure.elements.body.BodyType.BodyType
import infrastructure.propertybox.body.BodyPropertiesBox

class Body(name: String,
           var bodyType: BodyType = BodyType.RDF,
           var content: String = ""
          ) extends Element(name) {
  val propertiesBox = new BodyPropertiesBox()
}
