package infrastructure.controller.action

import infrastructure.controller.InfrastructureController
import infrastructure.drawingpane.DrawingPane
import infrastructure.elements.action.Action
import infrastructure.id.IdGenerator

class ActionListener(action: Action, infrastructureController: InfrastructureController, drawingPane: DrawingPane, idGenerator: IdGenerator) {
  private val propertiesBox = action.propertiesBox

  private val shape = action.stateActionPane

  propertiesBox.setOnActionNameChanged(name => {
    //TODO: notify the model
    action.name = name

    propertiesBox.setTiltedPaneName(name)
    shape.setActionName(name)
  })

  propertiesBox.setOnAbsoluteUriChanged(absoluteUri => {
    //TODO: notify the model
    action.absoluteUri = absoluteUri
  })

  propertiesBox.setOnUriTypeChanged(uriType => {
    //TODO: notify the model
    action.uriType = uriType
    
    propertiesBox.setUriType(uriType)
  })

  new PrototypeUriListener(action.prototypeUri)
}
