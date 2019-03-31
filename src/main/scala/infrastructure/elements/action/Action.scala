package infrastructure.elements.action

import infrastructure.elements.Element
import infrastructure.elements.action.ActionType.ActionType
import infrastructure.elements.action.UriType.UriType

class Action(id: String, var actionType: ActionType, var name: String, var uriType: UriType, var absoluteUri: String, var prototypeUri: PrototypeUri, var body: Body) extends Element(id) {
  def this(id: String, actionType: ActionType, name: String, absoluteUri: String, body: Body) = this(id, actionType, name, UriType.ABSOLUTE, absoluteUri, new PrototypeUri("", List()), body)
  def this(id: String, actionType: ActionType, name: String, prototypeUri: PrototypeUri, body: Body) = this(id, actionType, name, UriType.PROTOTYPE, "", prototypeUri, body)
  def this(id: String, actionType: ActionType, name: String) = this(id, actionType, name, UriType.ABSOLUTE, "", new PrototypeUri("", List()), new Body(BodyType.RDF, ""))
}
