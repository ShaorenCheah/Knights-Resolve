package project.view

import project.MainApp
import project.modal._
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.text.Text
import scalafx.scene.input.{KeyCode, KeyEvent}
import scalafx.animation.AnimationTimer
import scalafx.geometry.Pos
import scalafx.scene.control.Label
import scalafx.scene.layout.FlowPane
import scalafxml.core.macros.sfxml

@sfxml
class GameController(val timerText: Text, val multiplierText: Text, val comboText: Text, val scoreText: Text, val livesText: Text, val arrowPane: FlowPane) {

  private var stage: PrimaryStage = _
  private var difficulty: Difficulty = Easy // Default difficulty
  private var score: Int = 0
  private var multiplier: Double = difficulty.baseMultiplier
  private var combo: Int = 0
  private var lives: Int = difficulty.lives
  private def arrowKeys: Set[KeyCode] = Set(KeyCode.Left, KeyCode.Up, KeyCode.Right, KeyCode.Down)

  private val difficultyMap: Map[String, Difficulty] = Map(
    "Easy" -> Easy,
    "Normal" -> Normal,
    "Hard" -> Hard
  )

  // Timer to set game duration
  private val gameDuration: Int = 60
  private var elapsedTime: Long = 0L
  private var timeLeft: Int = gameDuration
  private var timer: AnimationTimer = _

  // Create 5 fixed slots for arrows
  private val arrowLabels: Seq[Label] = Seq.fill(5)(new Label(""))

  // Add all arrow labels to the FlowPane
  arrowLabels.foreach(label => {
    label.setStyle("-fx-font-size: 24pt;") // Default font size
    arrowPane.children.add(label)
  })
  arrowPane.alignment = Pos.Center
  arrowPane.hgap = 10

  // Method to set the primary stage for the controller
  def setStage(stage: PrimaryStage): Unit = {
    this.stage = stage
  }

  // Set the difficulty and update game parameters
  def setDifficulty(difficultyName: String): Unit = {
    println(s"Attempting to set difficulty to: $difficultyName") // Debug statement
    difficultyMap.get(difficultyName) match {
      case Some(difficulty) =>
        this.difficulty = difficulty
        // Update game parameters based on new difficulty
        this.lives = difficulty.lives
        this.multiplier = difficulty.baseMultiplier
        this.combo = 0
        updateUI()
      case None =>
        throw new IllegalArgumentException(s"Invalid difficulty level: $difficultyName")
    }
  }

  // Initialize DirectionHandler
  private val directionHandler = new DirectionHandler()

  // Initialize the arrow sequence
  private def generateInitialArrows(): Unit = {
    val initialArrows = List.fill(3)(directionHandler.generateRandomArrow()) ++ List.fill(2)(KeyCode.Space)
    for (i <- initialArrows.indices) {
      arrowLabels(i).text = directionHandler.keyCodeToSymbol(initialArrows(i))
      // Reset font size for all arrows initially
      arrowLabels(i).setStyle("-fx-font-size: 24pt;")
    }
    // Set the expected arrow as the middle arrow
    updateExpectedArrow(initialArrows(2)) // Middle one is the expected one
  }

  // Update the arrow sequence
  def updateArrows(): Unit = {
    val newArrow = directionHandler.generateRandomArrow()
    // Shift arrows to the right, add new arrow to the front
    for (i <- (arrowLabels.length - 1) to 1 by -1) {
      arrowLabels(i).text = arrowLabels(i - 1).text.value
    }
    arrowLabels(0).text = directionHandler.keyCodeToSymbol(newArrow)

    // Update the expected arrow
    updateExpectedArrow(arrowLabels(2).text.value match {
      case "←" => KeyCode.Left
      case "↑" => KeyCode.Up
      case "→" => KeyCode.Right
      case "↓" => KeyCode.Down
      case _ => KeyCode.Space
    })
  }

  // Assuming `expectedArrow` and `arrowKeys` are defined somewhere
  private def expectedArrow: KeyCode = {
    arrowLabels(2).text.value match {
      case "←" => KeyCode.Left
      case "↑" => KeyCode.Up
      case "→" => KeyCode.Right
      case "↓" => KeyCode.Down
      case _ => KeyCode.Space
    }
  }

  // Set the expected arrow and enlarge it
  private def updateExpectedArrow(newExpectedArrow: KeyCode): Unit = {
    for (i <- arrowLabels.indices) {
      val labelText = arrowLabels(i).text.value
      // Only enlarge the middle arrow
      if (i == 2 && directionHandler.keyCodeToSymbol(newExpectedArrow) == labelText) {
        arrowLabels(i).setStyle("-fx-font-size: 48pt;") // Enlarge the expected arrow
      } else {
        arrowLabels(i).setStyle("-fx-font-size: 18pt;") // Reset font size for non-expected arrows
      }
    }
  }

  // Handle user input
  def onKeyPressed(event: KeyEvent): Unit = {
    if (event.code == expectedArrow) {
      combo += 1
      multiplier = difficulty.baseMultiplier + (Math.ceil(combo / 10.0) * difficulty.comboIncrement)
      score += (10 * multiplier).toInt
      updateArrows()
    } else if (arrowKeys.contains(event.code) && event.code != expectedArrow) {
      combo = 0
      multiplier = difficulty.baseMultiplier
      lives -= 1
    }
    updateUI()
  }

  // Update UI elements
  private def updateUI(): Unit = {
    timerText.text = s"$timeLeft s"
    multiplierText.text = f"Multiplier: x$multiplier%.1f"
    comboText.text = s"COMBO: $combo"
    scoreText.text = s"Score: $score"
    livesText.text = s"Lives: $lives"
  }


  // Start the game loop
  def startGame(): Unit = {
    generateInitialArrows() // Display the initial sequence of arrows

    timer = AnimationTimer { now =>
      if (elapsedTime == 0L) elapsedTime = now // Initialize elapsed time
      val currentTime = (now - elapsedTime) / 1e9.toInt
      timeLeft = (gameDuration.toLong - currentTime).toInt
      if (lives <= 0 || timeLeft <= 0) {
        timer.stop() // Stop the AnimationTimer
        endGame()
      }
      updateUI()
    }

    timer.start()
  }

  // End the game and display final score
  private def endGame(): Unit = {
    MainApp.showResult(this.score)
  }

}
