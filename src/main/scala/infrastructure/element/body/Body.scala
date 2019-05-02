package infrastructure.element.body

import infrastructure.element.Element
import infrastructure.element.body.BodyType.BodyType
import infrastructure.propertybox.body.BodyPropertiesBox

/**
  * Body data
  * @param name name of the body
  * @param bodyType type of the body
  * @param content content of the body
  */
class Body(name: String,
           var bodyType: BodyType = BodyType.RDF,
           var content: String = ""
          ) extends Element(name) {
  val propertiesBox = new BodyPropertiesBox()
}
