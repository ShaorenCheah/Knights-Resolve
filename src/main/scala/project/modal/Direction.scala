package project.modal

import scalafx.scene.image.{Image, ImageView}
import scalafx.scene.input.KeyCode
import scala.collection.mutable.ArrayBuffer
import scala.util.Random

class Direction {
  // List of possible arrow key directions by getting from sealed abstract class KeyCode
  private val arrowKeys = List(KeyCode.Left, KeyCode.Right, KeyCode.Up, KeyCode.Down)
  private var expectedArrow: KeyCode = KeyCode.Left // Default expected arrow

  // Variables for arrow display
  private val numSlots = 5
  val arrowImages: Seq[ImageView] = Seq.fill(numSlots)(new ImageView()) // Fill slots with ImageView

  // Lists of arrows generated to be input
  private val arrowKeysList: ArrayBuffer[KeyCode] = ArrayBuffer.fill(numSlots)(KeyCode.Space)

  // Choose random direction from arrowKeys list
  private def generateRandomArrow(): KeyCode = arrowKeys(Random.nextInt(arrowKeys.length))

  // Match type of KeyCode with respective image path
  private def keyCodeToImage(keyCode: KeyCode): Image = keyCode match {
    case KeyCode.Left => new Image(getClass.getResourceAsStream("/project/images/direction/arrow-left.png"))
    case KeyCode.Up => new Image(getClass.getResourceAsStream("/project/images/direction/arrow-up.png"))
    case KeyCode.Right => new Image(getClass.getResourceAsStream("/project/images/direction/arrow-right.png"))
    case KeyCode.Down => new Image(getClass.getResourceAsStream("/project/images/direction/arrow-down.png"))
    case _ => new Image(getClass.getResourceAsStream("/project/images/direction/space.png"))
  }

  // Returns expected arrow
  def getExpectedArrow: KeyCode = expectedArrow

  // Initialize 3 arrows when first start
  def initializeArrows(): Unit = {
    val initialArrows = List.fill(3)(generateRandomArrow()) ++ List.fill(2)(KeyCode.Space)
    arrowKeysList.indices.foreach(i => fillArrowPane(i, initialArrows(i)))
    updateExpectedArrow()
  }

  // Fill in arrow pane with correct images
  private def fillArrowPane(index: Int, keyCode: KeyCode): Unit = {
    // Update the arrow key and image for the specified index
    arrowKeysList(index) = keyCode
    arrowImages(index).image = keyCodeToImage(keyCode)
    arrowImages(index).setFitHeight(50)
    arrowImages(index).setFitWidth(50)
  }

  // Update arrows when a correct input entered
  def updateArrows(): Unit = {
    val newArrow = generateRandomArrow()
    for (i <- (arrowKeysList.length - 1) to 1 by -1) {
      arrowKeysList(i) = arrowKeysList(i - 1) // Shift arrows
      fillArrowPane(i, arrowKeysList(i))
    }
    arrowKeysList(0) = newArrow // Add new arrow at the start
    fillArrowPane(0, newArrow)
    updateExpectedArrow()
  }

  private def updateExpectedArrow(): Unit = {
    // Get their image path and index for each image and update
    arrowImages.zipWithIndex.foreach { case (imageView, index) =>
      if (index == 2 && arrowKeysList(index) == arrowKeysList(2)) {
        // Enlarge expected direction arrow
        imageView.setFitHeight(100)
        imageView.setFitWidth(100)
      } else {
        // Normal size for other arrows
        imageView.setFitHeight(50)
        imageView.setFitWidth(50)
      }
    }
    expectedArrow = arrowKeysList(2)
  }
}
