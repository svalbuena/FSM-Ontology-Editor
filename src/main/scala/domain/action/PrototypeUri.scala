package domain.action

import domain.Element

class PrototypeUri(name: String,
                   x: Double,
                   y: Double,
                   var structure: String = "",
                   var prototypeParameters: List[PrototypeUriParameter] = List()
                  ) extends Element(name, x, y) {

}
