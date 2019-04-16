package domain.action

import domain.Element
import domain.action.ActionType.ActionType
import domain.action.MethodType.MethodType
import domain.action.UriType.UriType

class Action(name: String,
             x: Double,
             y: Double,
             var actionType: ActionType = ActionType.ENTRY,
             var methodType: MethodType = MethodType.GET,
             var uriType: UriType = UriType.ABSOLUTE,
             var absoluteUri: String = "",
             var prototypeUri: PrototypeUri,
             var timeout: Int = 0,
             var body: Body
            ) extends Element(name, x, y)  {

}
