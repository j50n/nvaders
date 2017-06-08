package com.lilypepper.nvaders

import java.time.LocalTime
import java.util.UUID

import org.scalajs.dom._

import scala.scalajs.js.JSApp
import scalatags.JsDom.TypedTag
import SvgDefinitions.{ScreenId, SmallInvId, MediumInvId, LargeInvId}

case class Rect(x: Int, y: Int, w: Int, h: Int)

case class Invader(var bounds: Rect) {
  val id: String = UUID.randomUUID().toString
}

object NVaders extends JSApp {
  val bundle = scalatags.JsDom

  val HRez = 240
  val VRez = 336


  def main(): Unit = {
    import bundle.implicits._
    import bundle.svgAttrs._
    import bundle.svgTags._
    import bundle.tags._

    document.body.appendChild(SvgDefinitions.definitions.render)
    document.body.appendChild(doSvg().render)

    for {
      i <- 0 until 11
      j <- 0 until 5
    } {
      if (j == 0) {
        document.getElementById(ScreenId).appendChild(
          use(xLinkHref := s"#${SmallInvId(1)}", transform := s"translate(${i * 16}, ${j * 12})").render
        )
      } else if (j == 1 || j == 2) {
        document.getElementById(ScreenId).appendChild(
          use(xLinkHref := s"#${MediumInvId(0)}", transform := s"translate(${i * 16}, ${j * 12})").render
        )
      } else {
        document.getElementById(ScreenId).appendChild(
          use(xLinkHref := s"#${LargeInvId(0)}", transform := s"translate(${i * 16}, ${j * 12})").render
        )
      }
    }

    document.body.appendChild(div(id := "debug").render)

    new AnimationFrame {
      override def callback(time: Long): Unit = {
        document.getElementById("debug").asInstanceOf[org.scalajs.dom.html.Div].textContent = (time % 60).toString
      }
    }
  }

  def doSvg(): TypedTag[svg.SVG] = {
    import bundle.implicits._
    import bundle.svgAttrs._
    import bundle.svgTags._

    svg(id := ScreenId, width := HRez, height := VRez, viewBox := s"0 0 $HRez $VRez",
      defs(),
      rect(x := 0, y := 0, width := HRez, height := VRez, style := "stroke:blue; stroke-width:0.5px; fill:cyan;")
    )
  }
}
