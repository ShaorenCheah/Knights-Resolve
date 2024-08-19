package project.modal
import scalafx.scene.media.AudioClip

object Audio {
  private var currentBgm: Option[AudioClip] = None

  def playBgm(bgmPath: String): Unit = {
    stopBgm() // Stop any currently playing BGM
    val newBgm = new AudioClip(getClass.getResource(bgmPath).toString)
    newBgm.volume = 0.3
    newBgm.play()
    currentBgm = Some(newBgm)
  }

  def stopBgm(): Unit = {
    currentBgm.foreach(_.stop())
    currentBgm = None
  }

  def playSfx(sfxPath: String): Unit = {
    val sfx = new AudioClip(getClass.getResource(sfxPath).toString)
    sfx.play()
  }
}