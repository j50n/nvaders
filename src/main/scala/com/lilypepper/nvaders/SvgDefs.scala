package com.lilypepper.nvaders

import java.util.UUID

import org.scalajs.dom.document
import org.scalajs.dom.svg.{G, Path, RectElement}

/**
  * Global SVG `defs` for all the animation cells in the game.
  *
  * SVG `defs` are accessed exclusively by their Id, by
  * referencing them in `use` elements. You never manipulate the
  * definition directly from the DOM.
  *
  * The SVG definitions element is automatically inserted
  * into the document body on initialization and are not
  * recreated at any point.
  *
  * @author Jason Smith
  */
object SvgDefs {
  private val bundle = scalatags.JsDom

  import bundle.implicits._
  import bundle.svgAttrs._
  import bundle.{TypedTag, svgTags => svg}

  type CellId = String

  val SmallInvId: IndexedSeq[CellId] = IndexedSeq(s"Inky1-${UUID.randomUUID()}", s"Inky2-${UUID.randomUUID()}")
  val MediumInvId: IndexedSeq[CellId] = IndexedSeq(s"Pinky1-${UUID.randomUUID()}", s"Pinky2-${UUID.randomUUID()}")
  val LargeInvId: IndexedSeq[CellId] = IndexedSeq(s"Blinky1-${UUID.randomUUID()}", s"Blinky2-${UUID.randomUUID()}")

  val Bomb1Id: IndexedSeq[CellId] = for(i <- 1 to 5) yield s"Bomb$i-${UUID.randomUUID()}"

  val UFOId: IndexedSeq[CellId] = IndexedSeq(s"Clyde1-${UUID.randomUUID()}", s"Clyde2-${UUID.randomUUID()}")
  val CannonId: CellId = s"Cannon-${UUID.randomUUID()}"
  val LaserId: CellId = s"Laser-${UUID.randomUUID()}"
  val BarrierId: CellId = s"Barrier=${UUID.randomUUID()}"

  private val D = "l0,1"
  private val U = "l0,-1"
  private val R = "l1,0"
  private val L = "l-1,0"

  private def pix(xp: Int, yp: Int) : svg.ConcreteHtmlTag[Path] =
    //svg.rect(x := xp, y:= yp, width:= 1, height:= 1)
  svg.path(d:=s"M$xp,$yp l1,0 l0,1 l-1,0 Z")

  private val definitions: TypedTag[org.scalajs.dom.svg.SVG] =
    svg.svg(style := "display:none",
      svg.defs(
        Seq() ++
          Seq(
            svg.ellipse(id := UFOId(0), cx := 8, cy := 4, rx := 8, ry := 4, style := "stroke:red;fill:green;"),
            svg.ellipse(id := UFOId(1), cx := 8, cy := 4, rx := 8, ry := 4, style := "stroke:red;fill:blue;")
          ) ++
          smallInvader ++ mediumInvader ++ largeInvader ++
          bomb1 :+
          cannon :+ laser
          :+ barrierPixel: _*
      )
    )

  document.body.appendChild(SvgDefs.definitions.render)

  private def cannon: svg.ConcreteHtmlTag[G] =
    svg.g(id := CannonId, `class` := "cannon",
      svg.path(
        d := Seq("M0,4", "l0,4", "l13,0", "l0,-4", L, U, "l-4,0",
          U, U, L, U, L, D, L, D, D, "l-4,0", D, "Z").mkString(" ")))

  private def laser: svg.ConcreteHtmlTag[G] =
    svg.g(id := LaserId, `class` := "laser", svg.path(d := "M0,0 l0,8 l1,0 l0,-8 Z"))

  private def barrierPixel: svg.ConcreteHtmlTag[RectElement] =
    svg.rect(id := BarrierId, `class` := "barrier-pixel", x := 0, y := 0, width := 1, height := 1)

  private def smallInvader: Seq[svg.ConcreteHtmlTag[G]] =
    Seq(
      svg.g(id := SmallInvId(0),
        `class` := "invader",
        svg.path(d :=
          Seq("M3,0",
            R, R, D, R, D, R, D, R, D, D, L, D, R, D, L, D,
            L, U, R, U, L, U, L, D, L, L, U, L, D, L, D, R,
            D, L, U, L, U, R, U, L, U, U, R, U, R, U, R, "Z",
            "M2,3", R, D, L, "Z",
            "M5,3", R, D, L, "Z").mkString(" "))
      ),
      svg.g(id := SmallInvId(1),
        `class` := "invader",
        svg.path(d :=
          Seq("M3,0",
            R, R, D, R, D, R, D, R, D, D, L, L, D, R, D, R,
            D, L, U, L, D, L, U, L, L, D, L, U, L, D, L, U,
            R, U, R, U, L, L, U, U, R, U, R, U, R,
            "Z",
            "M3,5", R, R, D, L, L, "Z",
            "M2,6", R, D, L, "Z",
            "M5,6", R, D, L, "Z", "M2,3", R, D, L, "Z",
            "M5,3", R, D, L, "Z").mkString(" "))
      )
    )

  private def mediumInvader: Seq[svg.ConcreteHtmlTag[G]] =
    Seq(
      svg.g(id := MediumInvId(0),
        `class` := "invader",
        svg.path(d := Seq(
          "M0,4", R, U, R, U, R, U, L, U, R, D, R, D, "l3,0",
          U, R, U, R, D, L, D, R, D, R, D, R,
          "l0,3", L, U, U, L, D, D,
          L, D, L, L, U, R, R, U, "l-5,0",
          D, R, R, D, L, L, U, L,
          U, U, L, D, D, L, "Z",
          "M3,3", R, D, L, "Z",
          "M7,3", R, D, L, "Z").mkString(" "))
      ),
      svg.g(id := MediumInvId(1),
        `class` := "invader",
        svg.path(d := Seq(
          "M0,1",
          R, D, D, R,
          U, R, U, L, U, R, D, R, D, "l3,0",
          U, R, U, R, D, L, D, R, D, R, U, U, R,
          "l0,4", L, D, L, D, R, D,
          L, U, L, U, "l-5,0", D, L, D, L, U, R, U, L,
          U, L,
          "Z",
          "M3,3", R, D, L, "Z",
          "M7,3", R, D, L, "Z").mkString(" "))
      )
    )

  private def largeInvader: Seq[svg.ConcreteHtmlTag[G]] =
    Seq(
      svg.g(id := LargeInvId(0),
        `class` := "invader",
        svg.path(d := Seq(
          "M3,3", R, R, D, L, L, "Z",
          "M7,3", R, R, D, L, L, "Z",
          "M5,5", R, R, D, L, L, "Z",
          "M0,2",
          R, U, "l3,0", U, "l4,0", D, "l3,0", D, R, "l0,3",
          L, L, D, R, D, L, D, L, L, U, R, U, L, L, D, L, L,
          U, L, L, D, R, D, L, L, U, L, U, R, U, L, L,
          "Z"
        ).mkString(" "))
      ),
      svg.g(id := LargeInvId(1),
        `class` := "invader",
        svg.path(d := Seq(
          "M3,3", R, R, D, L, L, "Z",
          "M7,3", R, R, D, L, L, "Z",
          "M5,5", R, R, D, L, L, "Z",
          "M0,2",
          R, U, "l3,0", U, "l4,0", D, "l3,0", D, R, "l0,3",
          "l-3,0", D, R, D, R, R, D, L, L, U, L, L, U,
          L, D, L, L, U, L, D, L, L, D, L, L,
          U, R, R, U, R, U, "l-3,0",
          "Z"
        ).mkString(" "))
      )
    )

  private def bomb1: Seq[svg.ConcreteHtmlTag[G]] =
    Seq(
      svg.g(id := Bomb1Id(0),
        `class` := "bomb",
        pix(1,0),
        pix(2,1),
        pix(1,2),
        pix(0,3),
        pix(1,4),
        pix(2,5),
        pix(1,6),
        pix(0,7)
      ),

      svg.g(id := Bomb1Id(1),
        `class` := "bomb",
        pix(2,0),
        pix(1,1),
        pix(0,2),
        pix(1,3),
        pix(2,4),
        pix(1,5),
        pix(0,6),
        pix(1,7)
      ),

      svg.g(id := Bomb1Id(2),
        `class` := "bomb",
        pix(1,0),
        pix(0,1),
        pix(1,2),
        pix(2,3),
        pix(1,4),
        pix(0,5),
        pix(1,6),
        pix(2,7)
      ),

      svg.g(id := Bomb1Id(3),
        `class` := "bomb",
        pix(0,0),
        pix(1,1),
        pix(2,2),
        pix(1,3),
        pix(0,4),
        pix(1,5),
        pix(2,6),
        pix(1,7)
      ),

      svg.g(id := Bomb1Id(4),
        `class` := "bomb",
        pix(1,0),
        pix(2,1),
        pix(1,2),
        pix(0,3),
        pix(1,4),
        pix(2,5),
        pix(1,6),
        pix(0,7)
      )
    )
}
