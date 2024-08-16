package project

import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.Scene
import scalafx.Includes._
import scalafxml.core.{FXMLLoader, NoDependencyResolver}
import project.view.HomepageController
import javafx.{scene => jfxs}

object MainApp extends JFXApp {

  // Initialize empty controllers for views
  var homepageController: Option[HomepageController#Controller] = None

  // Method to initialize .fxml files which returns roots and controller
  private def loadFXML[A](fileName: String): (jfxs.Parent, A) = {
    val resource = getClass.getResource(fileName)
    if (resource == null) {
      throw new RuntimeException(s"Cannot load resource: $fileName. Make sure the file exists in the correct directory.")
    }
    val loader = new FXMLLoader(resource, NoDependencyResolver)
    loader.load()
    val root = loader.getRoot[jfxs.Parent]
    val controller = loader.getController[A]
    (root, controller)
  }

  // Display homepage when first launch
  def showHomepage() = {
    val (roots1, controller) = loadFXML[HomepageController#Controller]("/project/view/Homepage.fxml")
    homepageController = Option(controller)
    stage = new PrimaryStage {
      title = "Rhythm"
      scene = new Scene {
        root = roots1
      }
    }
  }
  showHomepage()

  // Display game after clicking start
  def showGame(): Unit = {

  }
}