package com.lilypepper.nvaders

import java.time.LocalTime
import java.util.UUID

import org.scalajs.dom._

import scala.scalajs.js.JSApp
import scalatags.JsDom.TypedTag
import SvgDefinitions.{LargeInvId, MediumInvId, ScreenId, SmallInvId, CannonId}

import scala.annotation.tailrec


object NVaders extends JSApp {
  val bundle = scalatags.JsDom

  val HRez = 240
  val VRez = 336

  val InvXSpacing = 16
  val InvYSpacing = 12

  val InvXStep = 2

  case class Point(x: Int, y: Int)

  case class Size(w: Int, h: Int)

  case class Invader(var location: Point, dimensions: Size, animationSeq: AnimationSeq) {

    import NVaders.bundle.svgTags._

    val id: String = UUID.randomUUID().toString

    def update(): Unit = {
      element.setAttribute("href", s"#${animationSeq.value}")
      element.setAttribute("transform", s"translate(${location.x},${location.y})")
    }

    val element: org.scalajs.dom.svg.Use = use().render

    document.getElementById(ScreenId).appendChild(element)
  }

  case class Cannon(var location: Point) {

    import NVaders.bundle.svgTags._

    val dimensions: Size = Size(13, 8)

    val id: String = UUID.randomUUID().toString

    def update(): Unit = {
      element.setAttribute("transform", s"translate(${location.x},${location.y})")
    }

    val element: org.scalajs.dom.svg.Use = use().render
    element.setAttribute("href", s"#${CannonId}")

    document.getElementById(ScreenId).appendChild(element)
  }


  case class AnimationSeq(refs: Seq[String]) {
    require(refs.nonEmpty, "Can't create an animation sequence with 0 elements.")

    private var currentIndex = 0

    def next: String = {
      currentIndex = (currentIndex + 1) % refs.length
      value
    }

    def value: String = refs(currentIndex)
  }

  def main(): Unit = {
    //    import bundle.implicits._
    //    import bundle.svgAttrs._
    //    import bundle.svgTags._
    //    import bundle.tags._

    document.body.appendChild(SvgDefinitions.definitions.render)
    document.body.appendChild(doSvg().render)

    val cannon = Cannon(Point(20, 200))
    cannon.update()

    var invaders: Map[String, Invader] = Map()

    trait Advancer {
      def frame(): Unit
    }

    var advancer: Option[Advancer] = None

    case class CreateInvaders(yPos: Int) extends Advancer {
      private var aliens = {
        (for (i <- 0 until 11) yield {
          Invader(Point(i * InvXSpacing + 2, yPos), Size(8, 8), AnimationSeq(SmallInvId))
        }) ++
          (for {j <- 0 until 2; i <- 0 until 11} yield {
            Invader(Point(i * InvXSpacing, yPos + (j + 1) * InvYSpacing), Size(10, 8), AnimationSeq(MediumInvId))
          }) ++
          (for {j <- 0 until 2; i <- 0 until 11} yield {
            Invader(Point(i * InvXSpacing, yPos + (j + 3) * InvYSpacing), Size(10, 8), AnimationSeq(LargeInvId))
          })
      }.sortBy(n => (-n.location.y, n.location.x))

      override def frame(): Unit = {
        if (aliens.nonEmpty) {
          val alien = aliens.head
          invaders += alien.id -> alien
          alien.update()

          aliens = aliens.tail
        } else {
          advancer = Some(AdvanceInvadersRight())
        }
      }
    }

    trait AbstractAdvanceInvaders {
      def sorted(ns: IndexedSeq[Invader]): IndexedSeq[Invader]

      protected var aliens: IndexedSeq[String] = sorted(invaders.values.toIndexedSeq).map(_.id)

      @tailrec
      final def nextAlien(): Option[Invader] = {
        if (aliens.isEmpty) {
          None
        } else {
          val n = aliens.head
          aliens = aliens.tail

          if (invaders contains n) {
            Some(invaders(n))
          } else {
            nextAlien()
          }
        }
      }

    }

    case class AdvanceInvadersRight() extends Advancer with AbstractAdvanceInvaders {
      override def sorted(ns: IndexedSeq[Invader]): IndexedSeq[Invader] =
        ns.sortBy { n => (-n.location.y, n.location.x) }

      override def frame(): Unit = {
        val alien = nextAlien()

        if (alien.nonEmpty) {
          alien.foreach { n =>
            n.location = Point(alien.get.location.x + InvXStep, alien.get.location.y)
            n.animationSeq.next
            n.update()
          }
        }

        if (aliens.isEmpty) {
          val max = invaders.values.map(n => n.location.x + n.dimensions.w).max

          if (max >= HRez - 3) {
            advancer = Some(AdvanceInvadersDown(AdvanceInvadersLeft()))
          } else {
            advancer = Some(AdvanceInvadersRight())
          }
        }
      }
    }

    case class AdvanceInvadersLeft() extends Advancer with AbstractAdvanceInvaders {
      override def sorted(ns: IndexedSeq[Invader]): IndexedSeq[Invader] =
        ns.sortBy { n => (-n.location.y, -n.location.x) }

      override def frame(): Unit = {
        val alien = nextAlien()

        if (alien.nonEmpty) {
          alien.foreach { n =>
            n.location = Point(alien.get.location.x - InvXStep, alien.get.location.y)
            n.animationSeq.next
            n.update()
          }
        }

        if (aliens.isEmpty) {
          val min = invaders.values.map(_.location.x).min


          if (min <= 0 + 3) {
            advancer = Some(AdvanceInvadersDown(AdvanceInvadersRight()))
          } else {
            advancer = Some(AdvanceInvadersLeft())
          }
        }
      }
    }

    case class AdvanceInvadersDown(nextAdvancer: Advancer) extends Advancer with AbstractAdvanceInvaders {
      override def sorted(ns: IndexedSeq[Invader]): IndexedSeq[Invader] =
        ns.sortBy { n => (n.location.x, n.location.y) }

      override def frame(): Unit = {
        val alien = nextAlien()

        if (alien.nonEmpty) {
          alien.foreach { n =>
            n.location = Point(alien.get.location.x, alien.get.location.y + InvYSpacing)
            n.animationSeq.next
            n.update()
          }
        }

        if (aliens.isEmpty) {
          advancer = Some(nextAdvancer)
        }
      }
    }

    advancer = Some(CreateInvaders(100))

    new AnimationFrame {
      override def callback(time: Long): Unit = {
        //if (time % 2 == 0) {
        advancer.foreach(_.frame())
        //}

        //document.getElementById("debug").asInstanceOf[org.scalajs.dom.html.Div].textContent = (time % 60).toString
      }
    }
  }

  def doSvg(): TypedTag[svg.SVG] = {
    import bundle.implicits._
    import bundle.svgAttrs._
    import bundle.svgTags._

    svg(id := ScreenId, width := HRez, height := VRez, viewBox := s"0 0 $HRez $VRez",
      defs(),
      rect(x := 0, y := 0, width := HRez, height := VRez, style := "stroke:blue; stroke-width:1px; fill:none;")
    )
  }
}
