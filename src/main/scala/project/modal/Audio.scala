package project.modal

import scalafx.scene.media.AudioClip

object Audio {
  private var currentBgm: Option[AudioClip] = None
  private var bgmVolume: Double = 0.3
  private var sfxVolume: Double = 0.5

  private val storedSfx = Map(
    "click" -> new AudioClip(getClass.getResource("/project/audio/click.mp3").toString),
    "slash" -> new AudioClip(getClass.getResource("/project/audio/slash.mp3").toString),
    "kill" -> new AudioClip(getClass.getResource("/project/audio/kill.mp3").toString),
    "hurt" -> new AudioClip(getClass.getResource("/project/audio/hurt.mp3").toString)
  )

  storedSfx.values.foreach(_.volume = sfxVolume)

  def playBgm(bgmPath: String): Unit = {
    stopBgm() // Stop any currently playing BGM
    val newBgm = new AudioClip(getClass.getResource(bgmPath).toString)
    newBgm.volume = bgmVolume
    newBgm.play()
    currentBgm = Some(newBgm)
  }

  def stopBgm(): Unit = {
    currentBgm.foreach(_.stop())
    currentBgm = None
  }

  def playSfx(sfxKey: String): Unit = {
    storedSfx.get(sfxKey).foreach(_.play())
  }

  def buttonClick(): Unit = playSfx("click")

  def playSlash(): Unit = playSfx("slash")

  def playKill(): Unit = playSfx("kill")

  def playHurt(): Unit = playSfx("hurt")
}
