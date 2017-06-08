package com.lilypepper.nvaders

import org.scalajs.dom._
import AnimationFrame._

/**
  * Companion.
  */
object AnimationFrame {
  private val JPS = 16.0 + 2.0/3.0

  /**
    * The number of jiffies (60th of a second) since midnight, January 1, 1970.
    * @return The number of jiffies (60th of a second) since midnight, January 1, 1970.
    */
  def getCurrentTimeJiffy: Long = (System.currentTimeMillis() / JPS).toLong
}

/**
  * Callback within an animation frame, guaranteed 60 FPS processing (for the model) even
  * if the screen can't update that fast. The constant FPS is important for the game.
  *
  * Override [[callback()]] to provide the callback function.
  * Callbacks start immediately.
  * Call [[stop()]] when/if you want to stop animation.
  */
abstract class AnimationFrame {
  private var stopped = false

  private var latest : Long = getCurrentTimeJiffy

  /**
    * The callback function. This is guaranteed to
    * be called 60 times per second on all systems.
    * Keep in mind this means the model update needs
    * to be fast enough to keep up with this on
    * all browsers!
    * @param time Current time (as jiffy).
    */
  protected def callback(time: Long): Unit

  /**
    * Stop animation.
    */
  def stop(): Unit = stopped = true

  private def handler(timestamp: Double):Unit  = {
    val now = getCurrentTimeJiffy

    if(now - latest > 30){
      /*
       * Long pause, so they probably switched to another tab.
       * In this case, we don't want to update the model -
       * just continue where we left off.
       */
      latest = now
    } else {
      /*
       * Force model updates at 60 per second. This works
       * if the animation frames come too fast (120FPS) or
       * too slow (30FPS) by skipping or doubling up on updates.
       */
      while (latest < now) {
        latest += 1

        //TODO: Error handling?
        callback(latest)
      }
    }

    if(!stopped){
      window.requestAnimationFrame(handler)
    }
  }

  window.requestAnimationFrame(handler)
}
