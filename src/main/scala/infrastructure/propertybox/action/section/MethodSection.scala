package infrastructure.propertybox.action.section

import infrastructure.element.action.MethodType
import infrastructure.element.action.MethodType.MethodType
import javafx.scene.control.{ComboBox, Label}
import javafx.scene.layout.HBox

class MethodSection extends HBox {
  private val methodTypeLabel = new Label()
  methodTypeLabel.setText("Request method type:")

  private val methodTypeComboBox = new ComboBox[MethodType]()
  methodTypeComboBox.getItems.addAll(
    MethodType.GET,
    MethodType.POST
  )

  getChildren.addAll(methodTypeLabel, methodTypeComboBox)


  def setMethodType(methodType: MethodType): Unit = methodTypeComboBox.getSelectionModel.select(methodType)

  def setOnMethodTypeChanged(methodTypeChangedHandler: MethodType => Unit): Unit = {
    methodTypeComboBox.valueProperty().addListener(observable => {
      methodTypeChangedHandler(methodTypeComboBox.getValue)
    })
  }
}
