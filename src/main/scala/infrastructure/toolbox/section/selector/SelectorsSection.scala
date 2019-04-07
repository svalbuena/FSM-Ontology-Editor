package infrastructure.toolbox.section.selector

import infrastructure.toolbox.section.selector.mouse.{DeleteMouseSelector, NormalMouseSelector}
import javafx.scene.control.{Label, ToggleGroup}
import javafx.scene.layout.{FlowPane, VBox}

class SelectorsSection(toggleGroup: ToggleGroup) extends VBox {
  val sectionTitle = new Label
  sectionTitle.setText("Selectors")

  val normalMouseSelector = new NormalMouseSelector

  val mouseList = List(normalMouseSelector, new DeleteMouseSelector)

  val flowPane = new FlowPane()

  mouseList.foreach(mouse => {
    mouse.setToggleGroup(toggleGroup)
    flowPane.getChildren.add(mouse)
  })

  getChildren.add(sectionTitle)
  getChildren.add(flowPane)

  def setMouseToDefault(): Unit = {
    normalMouseSelector.setSelected(true)
  }
}
