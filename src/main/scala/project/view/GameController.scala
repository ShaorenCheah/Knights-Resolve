package project.view

import project.MainApp
import project.modal._
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.input.KeyEvent
import scalafx.geometry.Pos
import scalafx.scene.layout.FlowPane
import scalafx.scene.shape.{Circle, Rectangle}
import scalafx.scene.text.Text
import scalafx.scene.image.{Image, ImageView}
import scalafx.animation.AnimationTimer
import scalafxml.core.macros.sfxml

@sfxml
class GameController(
                      val timerText: Text,
                      val multiplierText: Text,
                      val comboText: Text,
                      val scoreText: Text,
                      val arrowPane: FlowPane,
                      val livesPane: FlowPane,
                      val knightView: ImageView,
                      val banditView: ImageView,
                      val rectangle: Rectangle,
                      val circle: Circle
                    ) {

  // Game state variables
  private var stage: PrimaryStage = _
  private var score: Int = 0
  private var combo: Int = 0
  private var victory = true

  // Difficulty settings and related variable
  private var difficulty: Difficulty = Easy // Initialize default difficulty from Difficulty.fxml
  private var multiplier: Double = difficulty.baseMultiplier
  private var lives: Int = difficulty.lives
  private val difficultyMap: Map[String, Difficulty] = Map(
    "Easy" -> Easy,
    "Normal" -> Normal,
    "Hard" -> Hard
  )

  // Direction handler to manage user input
  private val directionHandler = new Direction() // Handles arrow directions and user input from Direction.fxml

  // Character instances with associated images from Character.fxml
  private val knight = new Character(knightView, "knight")
  private val bandit = new Character(banditView, "bandit")

  // Image for displaying lives
  private val lifeImage = new Image(getClass.getResource("/project/images/heart_full.png").toExternalForm)

  // Game timing and duration variables
  private val gameDuration: Int = 60 // Total game duration
  private var elapsedTime: Long = 0L // Time elapsed since game started
  private var timeLeft: Int = gameDuration // Remaining time
  private var timer: AnimationTimer = _ // Timer for game loop

  // Initialize stage UI and BGM
  def initialize(stage: PrimaryStage, difficulty: String): Unit = {
    this.stage = stage
    Audio.playBgm("/project/audio/battle.mp3")
    setDifficulty(difficulty)
    initializeArrowView()
    directionHandler.initializeArrows() // Fill ImageView with Images
    knight.setIdle()
    bandit.setIdle()
    updateUI()
  }

  // Update UI elements
  private def updateUI(): Unit = {
    timerText.text = s"$timeLeft s"
    multiplierText.text = f"Multiplier: x$multiplier%.1f"
    comboText.text = s"COMBO: $combo"
    scoreText.text = s"Score: $score"
  }

  // Initialize ImageView for direction arrows
  private def initializeArrowView(): Unit = {
    directionHandler.arrowImages.foreach(imageView => {
      arrowPane.children.add(imageView)
    })
    arrowPane.hgap = 15
    arrowPane.alignment = Pos.Center
  }

  // Initialize difficulty values based on selected difficulty
  private def setDifficulty(difficultyName: String): Unit = {
    difficultyMap.get(difficultyName) match {
      case Some(difficulty) =>
        this.difficulty = difficulty
        this.lives = difficulty.lives
        this.multiplier = difficulty.baseMultiplier
        this.combo = 0
        updateLivesPane()
        updateUI()
      case None =>
        throw new IllegalArgumentException(s"Invalid difficulty level: $difficultyName")
    }
  }

  // Update lives pane images according to amount of lives available
  private def updateLivesPane(): Unit = {
    livesPane.getChildren.clear()
    for (_ <- 1 to lives) {
      val lifeImageView = new ImageView(lifeImage)
      lifeImageView.setFitHeight(35)
      lifeImageView.setFitWidth(35)
      livesPane.getChildren.add(lifeImageView)
    }
    livesPane.hgap = 20
    livesPane.setAlignment(Pos.Center)
  }

  // Update arrows within the arrow pane
  private def updateArrows(): Unit = {
    directionHandler.updateArrows() // Use Direction class method
    arrowPane.hgap = 30
    updateUI()
  }

  // Method to handle user input
  def onKeyPressed(event: KeyEvent): Unit = {

    // Correct Input
    if (event.code == directionHandler.getExpectedArrow) {
      combo += 1
      multiplier = difficulty.baseMultiplier + (Math.ceil(combo / 5.0) * difficulty.comboIncrement)
      score += (5 * multiplier).toInt
      updateArrows()
      if ((combo % 5) == 0) {
        Audio.playSlash()
        Audio.playKill()
        knight.performKill()
        bandit.banditDeath()
      } else {
        Audio.playSlash()
        knight.performAttack()
        bandit.performHurt()
      }
    }
    // Wrong Input
    else if (event.code.isArrowKey && event.code != directionHandler.getExpectedArrow) {
      combo = 0
      multiplier = difficulty.baseMultiplier
      lives -= 1
      updateLivesPane()
      knight.performHurt()
      bandit.performAttack()
      Audio.playHurt()
    }
    updateUI()
  }

  // Hide all UI elements at end of game
  private def hideElements() = {
    arrowPane.setVisible(false)
    circle.setVisible(false)
    rectangle.setVisible(false)
    timerText.setVisible(false)
    scoreText.setVisible(false)
    comboText.setVisible(false)
    multiplierText.setVisible(false)
  }

  def startGame(): Unit = {
    timer = AnimationTimer { now =>
      if (elapsedTime == 0L) elapsedTime = now // Initialize the timer to start from 0 for AnimationTimer
      val currentTime = (now - elapsedTime) / 1e9.toInt // Calculate elapsed time
      timeLeft = (gameDuration.toLong - currentTime).toInt // Update time left

      if (lives <= 0 || timeLeft <= 0) {
        timer.stop()
        directionHandler.arrowImages.foreach(_.image = null)
        if (lives == 0) {
          victory = false
          hideElements()
          bandit.performAttack()
          knight.knightDeath(() => endGame())
        } else {
          if (score == 0) {
            victory = false
          }
          endGame()
        }
      }
      updateUI()
    }
    timer.start()
  }

  private def endGame(): Unit = {
    MainApp.showResult(this.score, this.victory)
  }

}
