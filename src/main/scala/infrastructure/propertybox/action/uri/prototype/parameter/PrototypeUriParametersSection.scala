package infrastructure.propertybox.action.uri.prototype.parameter

import javafx.scene.control.{Button, Label, ScrollPane}
import javafx.scene.layout.{HBox, VBox}

class PrototypeUriParametersSection extends VBox {
  private val titleAndButtonPane = new HBox()

  private val titleLabel = new Label()
  titleLabel.setText("Parameters:")

  private val addParameterButton = new Button()
  addParameterButton.setText("Add parameter")

  titleAndButtonPane.getChildren.addAll(titleLabel, addParameterButton)

  private val parametersScrollPane = new ScrollPane()
  private val parametersPane = new VBox()
  parametersScrollPane.setContent(parametersPane)

  getChildren.addAll(titleAndButtonPane, parametersScrollPane)

  def addPrototypeUriParameter(prototypeUriParameterPropertiesBox: PrototypeUriParameterPropertiesBox): Unit = parametersPane.getChildren.add(prototypeUriParameterPropertiesBox)

  def removePrototypeUriParameter(prototypeUriParameterPropertiesBox: PrototypeUriParameterPropertiesBox): Unit = parametersPane.getChildren.remove(prototypeUriParameterPropertiesBox)

  def setOnAddParameterButtonClicked(callback: () => Unit): Unit = {
    addParameterButton.setOnMouseClicked(event => {
      callback()
    })
  }
}
