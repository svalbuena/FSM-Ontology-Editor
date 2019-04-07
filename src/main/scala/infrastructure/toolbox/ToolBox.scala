package infrastructure.toolbox

import infrastructure.toolbox.section.item.ItemsSection
import infrastructure.toolbox.section.selector.SelectorsSection
import javafx.scene.control.{Label, Toggle, ToggleGroup}
import javafx.scene.layout.VBox

class ToolBox extends VBox {
  setStyle("-fx-background-color: #b3c6b3")

  val toolBoxTitle = new Label()
  toolBoxTitle.setText("ToolBox")

  val toggleGroup = new ToggleGroup()

  val selectorsSection = new SelectorsSection(toggleGroup)

  val sectionList = List(selectorsSection, new ItemsSection(toggleGroup))

  getChildren.add(toolBoxTitle)

  sectionList.foreach(section => {
    getChildren.add(section)
  })

  setToolToDefault()

  def getSelectedTool: Toggle = {
    toggleGroup.getSelectedToggle
  }

  def setToolToDefault(): Unit = {
    selectorsSection.setMouseToDefault
  }
}
