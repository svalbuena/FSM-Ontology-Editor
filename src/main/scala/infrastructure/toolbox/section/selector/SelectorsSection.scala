package infrastructure.toolbox.section.selector

import infrastructure.toolbox.section.selector.mouse.{DeleteMouseSelector, NormalMouseSelector}
import javafx.scene.control.ToggleGroup
import javafx.scene.layout.{FlowPane, VBox}

/**
  * Section that contains the selectors
  *
  * @param toggleGroup toggle group where the selectors buttons are associated
  */
class SelectorsSection(toggleGroup: ToggleGroup) extends VBox {
  val normalMouseSelector = new NormalMouseSelector
  val deleteMouseSelector = new DeleteMouseSelector

  val mouseList = List(normalMouseSelector, deleteMouseSelector)

  val flowPane = new FlowPane()

  for (mouse <- mouseList) {
    mouse.setToggleGroup(toggleGroup)
    mouse.setOnAction(_ => {
      if (!mouse.isSelected) mouse.setSelected(true)
    })
    getChildren.add(mouse)
  }

  setStyle()

  def setMouseToDefault(): Unit = {
    normalMouseSelector.setSelected(true)
  }

  private def setStyle(): Unit = {
    flowPane.prefWidthProperty().bind(widthProperty())

    for (mouse <- mouseList) {
      mouse.prefWidthProperty().bind(widthProperty())
    }
  }
}
