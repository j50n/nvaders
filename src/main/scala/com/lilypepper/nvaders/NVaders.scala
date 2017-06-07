package com.lilypepper.nvaders

import java.time.LocalTime
import java.util.UUID

import org.scalajs.dom._

import scala.scalajs.js.JSApp
import scalatags.JsDom.TypedTag


object NVaders extends JSApp {
  val bundle = scalatags.JsDom

  val HRez = 240
  val VRez = 336


  def main(): Unit = {
    document.body.appendChild(doSvg().render)
  }

  def doSvg(): TypedTag[svg.SVG] = {
    import bundle.implicits._
    import bundle.svgAttrs._
    import bundle.svgTags._

    svg(width := HRez, height := VRez, viewBox := s"0 0 $HRez $VRez",
      rect(x:=0, y:=0, width:=HRez, height:=VRez, style:="stroke:blue; stroke-width:0.5px; fill:cyan;")
    )
  }
}
