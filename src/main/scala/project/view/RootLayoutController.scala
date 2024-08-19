package project.view

import project.MainApp
import project.modal.Audio
import scalafxml.core.macros.sfxml

@sfxml
class RootLayoutController {

  // Return to homepage
  def returnHome(): Unit = {
    Audio.buttonClick()
    MainApp.showHomepage()
  }

  // Show how to play
  def viewInstruction() = {
    Audio.buttonClick()
    MainApp.showInstruction()
  }

  // Exit the application
  def exit() = {
    Audio.buttonClick()
    System.exit(0)
  }

}
