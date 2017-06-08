package com.lilypepper.nvaders

import java.util.UUID

//import com.lilypepper.nvaders.NVaders._


object SvgDefinitions {
  val bundle = scalatags.JsDom

  import bundle.implicits._
  import bundle.svgAttrs._
  import bundle.svgTags._

  import bundle.TypedTag

  val ScreenId = s"SCREEN-${UUID.randomUUID()}"
  val SmallInvId = IndexedSeq(s"Inky1-${UUID.randomUUID()}", s"Inky2-${UUID.randomUUID()}")
  val MediumInvId = IndexedSeq(s"Pinky1-${UUID.randomUUID()}", s"Pinky2-${UUID.randomUUID()}")
  val LargeInvId = IndexedSeq(s"Blinky1-${UUID.randomUUID()}", s"Blinky2-${UUID.randomUUID()}")
  val UFOId = IndexedSeq(s"Clyde1-${UUID.randomUUID()}", s"Clyde2-${UUID.randomUUID()}")

  private val D = "l0,1"
  private val U = "l0,-1"
  private val R = "l1,0"
  private val L = "l-1,0"

  val definitions: TypedTag[org.scalajs.dom.svg.SVG] =
    svg(style := "display:none",
      defs(
        g(id := SmallInvId(0),
          style := "stroke:none;fill:blue;stroke-width:0.2px",
          path(d :=
            Seq("M3,0",
              R, R, D, R, D, R, D, R, D, D, L, D, R, D, L, D,
              L, U, R, U, L, U, L, D, L, L, U, L, D, L, D, R,
              D, L, U, L, U, R, U, L, U, U, R, U, R, U, R,
              "Z").mkString(" "),
            style := "stroke:black"),
          rect(x := 2, y := 3, width := 1, height := 1, style := "fill:white"),
          rect(x := 5, y := 3, width := 1, height := 1, style := "fill:white")
        ),
        g(id := SmallInvId(1),
          style := "stroke:none;fill:blue;stroke-width:0.2px",
          path(d :=
            Seq("M3,0",
              R, R, D, R, D, R, D, R, D, D, L, L, D, R, D, R,
              D, L, U, L, D, L, U, L, L, D, L, U, L, D, L, U,
              R, U, R, U, L, L, U, U, R, U, R, U, R,
              "Z",
              "M3,5", R, R, D, L, L, "Z",
              "M2,6", R, D, L, "Z",
              "M5,6", R, D, L, "Z").mkString(" "),
            style := "fill-rule:evenodd;stroke:black"),
          rect(x := 2, y := 3, width := 1, height := 1, style := "fill:white"),
          rect(x := 5, y := 3, width := 1, height := 1, style := "fill:white")
        ),

        ellipse(id := MediumInvId(0), cx := 6, cy := 5, rx := 6, ry := 5, style := "stroke:red;fill:green;"),
        ellipse(id := MediumInvId(1), cx := 6, cy := 5, rx := 6, ry := 5, style := "stroke:red;fill:blue;"),

        rect(id := LargeInvId(0), x := 0, y := 0, width := 12, height := 10, style := "stroke:red;fill:green;"),
        rect(id := LargeInvId(1), x := 0, y := 0, width := 12, height := 10, style := "stroke:red;fill:blue;"),

        ellipse(id := UFOId(0), cx := 8, cy := 4, rx := 8, ry := 4, style := "stroke:red;fill:green;"),
        ellipse(id := UFOId(1), cx := 8, cy := 4, rx := 8, ry := 4, style := "stroke:red;fill:blue;")
      )
    )
}
