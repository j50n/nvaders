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
  val CannonId = s"Cannon-${UUID.randomUUID()}"
  val LaserId = s"Laser-${UUID.randomUUID()}"

  private val D = "l0,1"
  private val U = "l0,-1"
  private val R = "l1,0"
  private val L = "l-1,0"

  val definitions: TypedTag[org.scalajs.dom.svg.SVG] =
    svg(style := "display:none",
      defs(
        g(id := SmallInvId(0),
          `class` := "invader",
          path(d :=
            Seq("M3,0",
              R, R, D, R, D, R, D, R, D, D, L, D, R, D, L, D,
              L, U, R, U, L, U, L, D, L, L, U, L, D, L, D, R,
              D, L, U, L, U, R, U, L, U, U, R, U, R, U, R, "Z",
              "M2,3", R, D, L, "Z",
              "M5,3", R, D, L, "Z").mkString(" "))
        ),
        g(id := SmallInvId(1),
          `class` := "invader",
          path(d :=
            Seq("M3,0",
              R, R, D, R, D, R, D, R, D, D, L, L, D, R, D, R,
              D, L, U, L, D, L, U, L, L, D, L, U, L, D, L, U,
              R, U, R, U, L, L, U, U, R, U, R, U, R,
              "Z",
              "M3,5", R, R, D, L, L, "Z",
              "M2,6", R, D, L, "Z",
              "M5,6", R, D, L, "Z", "M2,3", R, D, L, "Z",
              "M5,3", R, D, L, "Z").mkString(" "))
        ),

        g(id := MediumInvId(0),
          `class` := "invader",
          path(d:= Seq(
            "M0,4", R, U, R, U, R, U, L, U, R, D, R, D, "l3,0",
            U, R, U, R, D, L, D, R, D, R, D, R,
            "l0,3", L, U, U, L, D, D,
            L,D,L,L,U,R,R,U,"l-5,0",
            D,R,R,D,L,L,U,L,
            U,U,L,D,D,L, "Z",
            "M3,3", R, D, L, "Z",
            "M7,3", R, D, L, "Z").mkString(" "))
        ),

        g(id := MediumInvId(1),
          `class` := "invader",
          path(d:= Seq(
            "M0,1",
            R,D,D,R,
            U, R, U, L, U, R, D, R, D, "l3,0",
            U, R, U, R, D, L, D, R, D, R, U,U,R,
            "l0,4",L,D,L,D,R,D,
            L,U,L,U,"l-5,0",D,L,D,L,U,R,U,L,
            U,L,
            "Z",
            "M3,3", R, D, L, "Z",
            "M7,3", R, D, L, "Z").mkString(" "))
        ),

        g(id := LargeInvId(0),
          `class` := "invader",
          path(d:= Seq(
            "M3,3", R,R,D,L,L,"Z",
            "M7,3", R,R,D,L,L,"Z",
            "M5,5", R,R,D,L,L, "Z",
            "M0,2",
            R,U,"l3,0",U,"l4,0",D,"l3,0",D,R,"l0,3",
            L,L,D,R,D,L,D,L,L,U,R,U,L,L,D,L,L,
            U,L,L,D,R,D,L,L,U,L,U,R,U, L,L,
            "Z"
            ).mkString(" "))
        ),

        g(id := LargeInvId(1),
          `class` := "invader",
          path(d:= Seq(
            "M3,3", R,R,D,L,L,"Z",
            "M7,3", R,R,D,L,L,"Z",
            "M5,5", R,R,D,L,L, "Z",
            "M0,2",
            R,U,"l3,0",U,"l4,0",D,"l3,0",D,R,"l0,3",
            "l-3,0",D,R,D,R,R,D,L,L,U,L,L,U,
            L,D,L,L,U,L,D,L,L,D,L,L,
            U,R,R,U,R,U,"l-3,0",
            "Z"
          ).mkString(" "))
        ),

        ellipse(id := UFOId(0), cx := 8, cy := 4, rx := 8, ry := 4, style := "stroke:red;fill:green;"),
        ellipse(id := UFOId(1), cx := 8, cy := 4, rx := 8, ry := 4, style := "stroke:red;fill:blue;"),

        path(id := CannonId, d := Seq(
          "M0,4", "l0,4", "l13,0", "l0,-4",
          L, U, "l-4,0", U, U, L, U,
          L, D, L, D, D, "l-4,0", D, "Z").mkString(" "), style := "stroke:none;fill:green"),

        path(id := LaserId, d:= "M0,0 l0,8 l1,0 l0,-8 Z")
      )
    )
}
