package toolbox

import javafx.scene.control.Label
import javafx.scene.layout.VBox
import toolbox.sections.`object`.ObjectsSection
import toolbox.sections.selector.SelectorsSection

object ToolBox extends VBox {
  val toolBoxTitle = new Label()
  toolBoxTitle.setText("ToolBox")

  getChildren.add(toolBoxTitle)
  getChildren.add(SelectorsSection)
  getChildren.add(ObjectsSection)
}
