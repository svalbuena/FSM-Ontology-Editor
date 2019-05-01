package infrastructure.toolbox

import infrastructure.toolbox.section.item.ItemsSection
import infrastructure.toolbox.section.selector.SelectorsSection
import javafx.scene.control.{TitledPane, Toggle, ToggleGroup}
import javafx.scene.layout.VBox

class ToolBox extends TitledPane {
  setText("ToolBox")

  val toggleGroup = new ToggleGroup()

  val selectorsSection = new SelectorsSection(toggleGroup)
  val selectorsTitledPane = new TitledPane()
  selectorsTitledPane.setContent(selectorsSection)
  selectorsTitledPane.setText("Selectors")

  val itemsSection = new ItemsSection(toggleGroup)
  val itemsTitledPane = new TitledPane()
  itemsTitledPane.setContent(itemsSection)
  itemsTitledPane.setText("Items")

  val content = new VBox()
  content.getChildren.addAll(selectorsTitledPane, itemsTitledPane)

  setContent(content)

  setToolToDefault()

  setStyle()


  def getSelectedTool: Toggle = {
    toggleGroup.getSelectedToggle
  }

  def setToolToDefault(): Unit = {
    selectorsSection.setMouseToDefault()
  }

  private def setStyle(): Unit = {
    setCollapsible(false)

    getStyleClass.add("toolbox-titled-pane")

    content.getStyleClass.add("toolbox-vbox")

    selectorsTitledPane.getStyleClass.add("selectors-titled-pane")
    selectorsTitledPane.setCollapsible(false)

    itemsTitledPane.getStyleClass.add("items-titled-pane")
    itemsTitledPane.setCollapsible(false)
  }
}
