package infrastructure.filechooser

import javafx.stage.FileChooser.ExtensionFilter
import javafx.stage.{FileChooser, Stage}

/**
  * File chooser to select the fsm file to load and save
  *
  * @param stage stage of the application
  */
class FsmFileChooser(stage: Stage) {
  private val fileChooser = new FileChooser

  fileChooser.getExtensionFilters.addAll(
    new ExtensionFilter("RDF files", "*.ttl")
  )

  /**
    * Shows a window to select the fsm file to load
    *
    * @return the filepath if a file is selected
    */
  def askForFileToOpen(): Option[String] = {
    fileChooser.setTitle("Choose the FSM file to load")

    val selectedFile = fileChooser.showOpenDialog(stage)
    if (selectedFile != null) {
      val filename = selectedFile.getAbsolutePath

      Some(filename)
    } else {
      None
    }
  }

  /**
    * Shows a window to select the place to store the fsm
    *
    * @return the filepath if a place is selected
    */
  def askForFileToSave(): Option[String] = {
    fileChooser.setTitle("Choose where to save the FSM")

    val selectedFile = fileChooser.showSaveDialog(stage)
    if (selectedFile != null) {
      val filename = selectedFile.getAbsolutePath

      Some(filename)
    } else {
      None
    }
  }
}
