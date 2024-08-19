package project.view

import project.MainApp
import project.modal.Audio
import scalafxml.core.macros.sfxml

@sfxml
class HomepageController() {
  Audio.playBgm("/project/audio/home.mp3")

  def readInstruction() = {
    Audio.playSfx("/project/audio/click.mp3")
    MainApp.showInstruction()
  }

  def selectDifficulty(): Unit = {
    Audio.playSfx("/project/audio/click.mp3")
    MainApp.showDifficulty()
  }

  def exit() = {
    Audio.playSfx("/project/audio/click.mp3")
    System.exit(0)
  }
}
