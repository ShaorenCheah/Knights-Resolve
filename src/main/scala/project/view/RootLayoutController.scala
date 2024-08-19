package project.view

import project.MainApp
import project.modal.Audio
import scalafxml.core.macros.sfxml

@sfxml
class RootLayoutController {
  def returnHome(): Unit = {
    MainApp.showHomepage()
  }

  def viewInstruction() = {
    Audio.playSfx("/project/audio/click.mp3")
    MainApp.showInstruction()
  }

  def exit() = {
    Audio.playSfx("/project/audio/click.mp3")
    System.exit(0)
  }
}
