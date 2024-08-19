package project

import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.Scene
import project.view.{InstructionController, ResultController, GameController, DifficultyController, HomepageController}
import scalafx.Includes._
import scalafxml.core.{FXMLLoader, NoDependencyResolver}
import javafx.{scene => jfxs}


object MainApp extends JFXApp {

  // Initialize empty controllers for views
  var homepageController: Option[HomepageController#Controller] = None
  var difficultyController: Option[DifficultyController#Controller] = None
  var gameController: Option[GameController#Controller] = None
  var resultController: Option[ResultController#Controller] = None
  var instructionController: Option[InstructionController#Controller] = None

  // Method to initialize .fxml files which returns roots and controller

  private def loadFXML[A](fileName: String): (jfxs.Parent, A) = {
    val resource = getClass.getResource(fileName)
    val loader = new FXMLLoader(resource, NoDependencyResolver)
    loader.load()
    val root = loader.getRoot[jfxs.Parent]
    val controller = loader.getController[A]
    (root, controller)
  }

  // Display homepage when first launch
  def showHomepage(): Unit = {
    val (roots1, controller) = loadFXML[HomepageController#Controller]("view/Homepage.fxml")
    homepageController = Option(controller)
    stage = new PrimaryStage {
      title = "Knight's Resolve"
      resizable = false
      scene = new Scene {
        root = roots1
      }
    }
  }
  showHomepage()

  // Display difficulty selection
  def showDifficulty(): Unit = {
    val (roots2, controller) = loadFXML[DifficultyController#Controller]("view/Difficulty.fxml")
    difficultyController = Option(controller)
    stage = new PrimaryStage {
      title = "Knight's Resolve"
      resizable = false
      scene = new Scene {
        root = roots2
        difficultyController.foreach(controller => controller.initialize())
      }
    }
  }

  // Display the game scene
  def showGame(difficulty: String): Unit = {
    val (roots3, controller) = loadFXML[GameController#Controller]("/project/view/Game.fxml")
    gameController = Option(controller)
    stage = new PrimaryStage {
      title = "Knight's Resolve"
      resizable = false
      scene = new Scene {
        root = roots3
        gameController.foreach(controller =>{
          controller.setStage(stage)
          controller.setDifficulty(difficulty)
        })
      }
    }
    // Event handler to read user input
    stage.scene().onKeyPressed = { event =>
      gameController.foreach(controller => controller.onKeyPressed(event))
    }
    // Start the game
    gameController.foreach(controller => controller.startGame())
  }

  // Display game result
  def showResult(score: Int, victory: Boolean): Unit = {
    val (roots4, controller) = loadFXML[ResultController#Controller]("view/Result.fxml")
    resultController = Option(controller)
    stage = new PrimaryStage {
      title = "Knight's Resolve"
      resizable = false
      scene = new Scene {
        root = roots4
        resultController.foreach(controller => controller.initialize(score, victory))
      }
    }
  }

  // Display game guide
  def showInstruction(): Unit = {
    val (roots5, controller) = loadFXML[InstructionController#Controller]("view/Instruction.fxml")
    instructionController = Option(controller)
    stage = new PrimaryStage {
      title = "Knight's Resolve"
      resizable = false
      scene = new Scene {
        root = roots5
      }
    }
  }
}