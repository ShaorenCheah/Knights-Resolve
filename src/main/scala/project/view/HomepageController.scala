package project.view

import project.MainApp
import project.modal.Audio
import scalafxml.core.macros.sfxml

@sfxml
class HomepageController() {
  Audio.playBgm("/project/audio/home.mp3")

  def readInstruction() = {
    Audio.buttonClick()
    MainApp.showInstruction()
  }

  def selectDifficulty(): Unit = {
    Audio.buttonClick()
    MainApp.showDifficulty()
  }

  def exit(): Unit = {
    Audio.buttonClick()
    System.exit(0)
  }
}
