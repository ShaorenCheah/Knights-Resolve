package project.view

import project.MainApp
import project.modal.Audio
import scalafxml.core.macros.sfxml

@sfxml
class HomepageController() {
  Audio.playBgm("/project/audio/home.mp3")

  // Show how to play
  def readInstruction() = {
    Audio.buttonClick()
    MainApp.showInstruction()
  }

  // Show difficulty selection
  def selectDifficulty(): Unit = {
    Audio.buttonClick()
    MainApp.showDifficulty()
  }

  // Exit the application
  def exit(): Unit = {
    Audio.buttonClick()
    System.exit(0)
  }
}
