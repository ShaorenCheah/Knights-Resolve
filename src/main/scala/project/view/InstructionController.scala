package project.view

import project.MainApp
import project.modal.Audio
import scalafxml.core.macros.sfxml

@sfxml
class InstructionController {

  def back(): Unit = {
    Audio.playSfx("/project/audio/click.mp3")
    MainApp.showHomepage()
  }
}
