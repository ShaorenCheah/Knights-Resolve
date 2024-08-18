package project.view

import project.MainApp
import scalafx.scene.control.Button
import scalafx.scene.text.Text
import scalafxml.core.macros.sfxml

@sfxml
class ResultController(val scoreText: Text, val backButton: Button) {
  def initialize(score: Int): Unit = {
    scoreText.text = s"$score"
  }

  def retry(): Unit = {
    MainApp.showDifficulty()
  }

  def goBack(){
    MainApp.showHomepage()
  }

}
