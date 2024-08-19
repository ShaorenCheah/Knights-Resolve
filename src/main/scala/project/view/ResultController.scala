package project.view

import project.MainApp
import project.modal.Audio
import scalafx.scene.control.Button
import scalafx.scene.text.Text
import scalafxml.core.macros.sfxml

@sfxml
class ResultController(val scoreText: Text, val backButton: Button) {
  def initialize(score: Int, victory: Boolean): Unit = {
    if(victory){
      Audio.playBgm("/project/audio/victory.mp3")
    }else{
      Audio.playBgm("/project/audio/defeat.mp3")
    }

    scoreText.text = s"$score"
  }

  def retry(): Unit = {
    Audio.playSfx("/project/audio/click.mp3")
    MainApp.showDifficulty()
  }

  def goBack(){
    Audio.playSfx("/project/audio/click.mp3")
    MainApp.showHomepage()
  }

}
