package infrastructure.elements.action.body

import infrastructure.elements.Element
import infrastructure.elements.action.body.BodyType.BodyType
import infrastructure.propertybox.action.section.body.BodyPropertiesBox

class Body(name: String,
           var bodyType: BodyType = BodyType.RDF,
           var content: String = ""
          ) extends Element(name) {
  val propertiesBox = new BodyPropertiesBox()
}
