package infrastructure.controller.action

import infrastructure.elements.action.uri.prototype.PrototypeUri

class PrototypeUriListener(prototypeUri: PrototypeUri) {
  prototypeUri.propertiesBox.setOnStructureChanged(structure => {
    //TODO: notify the model
    prototypeUri.structure = structure
  })

  for (prototypeParameter <- prototypeUri.prototypeParameters) {
    new PrototypeParameterListener(prototypeParameter)
  }
}
