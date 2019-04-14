package infrastructure.elements.action.body

import infrastructure.elements.action.body.BodyType.BodyType
import infrastructure.propertybox.action.section.body.BodyPropertiesBox

class Body(var bodyType: BodyType = BodyType.RDF,
           var content: String = ""
          ) {
  val propertiesBox = new BodyPropertiesBox()
}
