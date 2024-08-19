package project.view

import project.MainApp
import project.modal.Audio
import scalafx.scene.control.Button
import scalafx.scene.text.Text
import scalafxml.core.macros.sfxml

@sfxml
class ResultController(val scoreText: Text, val resultText: Text) {

  // Initialize the UI
  def initialize(score: Int, victory: Boolean): Unit = {
    if(victory){
      Audio.playBgm("/project/audio/victory.mp3")
      resultText.text = "VICTORY"
    }else{
      Audio.playBgm("/project/audio/defeat.mp3")
      resultText.text = "GAME OVER"
    }

    scoreText.text = s"$score"
  }

  // Redirect to select difficulty
  def retry(): Unit = {
    Audio.buttonClick()
    MainApp.showDifficulty()
  }

  // Return to homepage
  def goBack(){
    Audio.buttonClick()
    MainApp.showHomepage()
  }

}
