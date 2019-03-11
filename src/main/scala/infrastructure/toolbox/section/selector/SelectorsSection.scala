package infrastructure.toolbox.section.selector

import javafx.scene.control.{Label, ToggleButton, ToggleGroup}
import javafx.scene.layout.{FlowPane, VBox}
import infrastructure.toolbox.section.selector.mouse.NormalMouseSelector

class SelectorsSection(toggleGroup: ToggleGroup) extends VBox {
  val sectionTitle = new Label
  sectionTitle.setText("Selectors")

  val normalMouseSelector = new NormalMouseSelector

  val mouseList = List(normalMouseSelector)

  val flowPane = new FlowPane()

  mouseList.foreach(mouse => {
    mouse.setToggleGroup(toggleGroup)
    flowPane.getChildren.add(mouse)
  })

  getChildren.add(sectionTitle)
  getChildren.add(flowPane)

  def setMouseToDefault: Unit = {
    normalMouseSelector.setSelected(true)
  }
}
