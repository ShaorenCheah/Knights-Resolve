package project.view

import project.MainApp
import project.modal.Audio
import scalafxml.core.macros.sfxml

@sfxml
class InstructionController {

  // Return back to homepage
  def back(): Unit = {
    Audio.buttonClick()
    MainApp.showHomepage()
  }
}
