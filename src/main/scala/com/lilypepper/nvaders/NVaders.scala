package com.lilypepper.nvaders

import java.util.UUID

import com.lilypepper.nvaders.SvgDefs._
import org.scalajs.dom._

import scala.annotation.tailrec
import scala.scalajs.js.JSApp
import scalatags.JsDom.TypedTag


object NVaders extends JSApp {
  val bundle = scalatags.JsDom

  val HRez = 240
  val VRez = 336

  val Scale = 1.0

  val InvXSpacing = 16
  val InvYSpacing = 12

  val InvXStep = 2

  val ArrowLeft = 37
  val ArrowRight = 39
  val Space = 32

  val ScreenId: Id = s"SCREEN-${UUID.randomUUID()}"

  case class Point(x: Int, y: Int)

  case class Size(w: Int, h: Int)

  trait Widget {

    import scalatags.JsDom.implicits._
    import scalatags.JsDom.svgAttrs.{id => idAttr}
    import scalatags.JsDom.svgTags.use

    var location: Point
    val dimensions: Size
    val animationSeq: AnimationSeq

    def layerId: String = ScreenId

    val id: String = UUID.randomUUID().toString

    val element: org.scalajs.dom.svg.Use = use(idAttr := id).render
    document.getElementById(layerId).appendChild(element)

    def updateView(): Unit = {
      element.setAttribute("href", s"#${animationSeq.value}")
      element.setAttribute("transform", s"translate(${location.x},${location.y})")
    }

    def destroy(): Unit = {
      if (element.parentNode != null) {
        element.parentNode.removeChild(element)
      }
    }

    override def finalize(): Unit = {
      try {
        destroy()
      } finally {
        super.finalize()
      }
    }
  }

  case class Invader(var location: Point, dimensions: Size, animationSeq: AnimationSeq) extends Widget

  case class Cannon(var location: Point) extends Widget {
    val animationSeq = AnimationSeq(Seq(CannonId))
    val dimensions: Size = Size(13, 8)
  }

  case class Laser(var location: Point) extends Widget {
    val animationSeq = AnimationSeq(Seq(LaserId))
    val dimensions: Size = Size(1, 8)
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
    //document.body.appendChild(SvgDefinitions.definitions.render)

    val playArea = doSvg().render
    document.body.appendChild(playArea)

    val cannon = Cannon(Point(0, VRez - 32))
    cannon.updateView()

    var arrowLeftToggle = false
    var arrowRightToggle = false
    var spaceToggle = false

    def stop(e: KeyboardEvent): Boolean = {
      e.preventDefault()
      e.stopPropagation()
      false
    }

    document.onkeyup = (e: KeyboardEvent) => {
      //      console.info(s"UP ${e.charCode} ${e.key} ${e.keyCode}")
      e.keyCode match {
        case ArrowLeft => arrowLeftToggle = false; stop(e)
        case ArrowRight => arrowRightToggle = false; stop(e)
        case Space => spaceToggle = false; stop(e)
        case _ =>
      }
    }

    document.onkeydown = (e: KeyboardEvent) => {
      //      console.info(s"DOWN ${e.charCode} ${e.key} ${e.keyCode}")
      e.keyCode match {
        case ArrowLeft => arrowLeftToggle = true; stop(e)
        case ArrowRight => arrowRightToggle = true; stop(e)
        case Space => spaceToggle = true; stop(e)
        case _ =>
      }
    }

    document.onkeypress = (e: KeyboardEvent) => {
      e.keyCode match {
        case ArrowLeft => stop(e)
        case ArrowRight => stop(e)
        case Space => stop(e)
        case _ =>
      }
    }

    def resizePlayArea(): Unit = {
      val playArea = document.getElementById(ScreenId).asInstanceOf[org.scalajs.dom.svg.SVG]
      val w = window.innerWidth
      val h = window.innerHeight

      val playW = 1.0 * h * HRez / VRez

      playArea.style.position = "absolute"
      playArea.style.top = s"${0}px"
      playArea.style.left = s"${w / 2 - Scale * playW / 2}px"

      playArea.setAttribute("height", s"${Scale * h}px")
      playArea.setAttribute("width", s"${Scale * playW}px")
    }

    resizePlayArea()

    window.onresize = (_: UIEvent) => {
      console.info(s"${window.innerWidth},${window.innerHeight}")
      resizePlayArea()
    }

    var invaders: Map[String, Invader] = Map()

    trait Advancer {
      def frame(): Unit
    }

    var advancer: Option[Advancer] = None

    var laserAdvancer: Option[LaserAdvancer] = None

    case class LaserAdvancer(location: Point) extends Advancer {
      val laser = Laser(location)

      override def frame(): Unit = {
        if (laser.location.y < -laser.dimensions.h) {
          laser.destroy()
          laserAdvancer = None
        } else {
          laser.location = laser.location.copy(y = laser.location.y - 3)
          laser.updateView()
        }
      }
    }

    Barrier.drawBarriers(275)

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
          alien.updateView()

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
            n.updateView()
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
            n.updateView()
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
            n.updateView()
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
        playArea.pauseAnimations()
        try {
          advancer.foreach(_.frame())

          if ((arrowLeftToggle && arrowRightToggle) || !(arrowLeftToggle || arrowRightToggle)) {
            //No movement.
          } else if (arrowLeftToggle) {
            if (cannon.location.x > 0) {
              cannon.location = cannon.location.copy(x = cannon.location.x - 1)
              cannon.updateView()
            }
          } else {
            if (cannon.location.x < HRez - cannon.dimensions.w) {
              cannon.location = cannon.location.copy(x = cannon.location.x + 1)
              cannon.updateView()
            }
          }

          if (spaceToggle && laserAdvancer.isEmpty) {
            laserAdvancer = Some(LaserAdvancer(cannon.location.copy(x = cannon.location.x + cannon.dimensions.w / 2)))
          }
          laserAdvancer.foreach{la =>
            la.frame()

            val laserLoc = la.laser.location

            val hits = invaders.values.filter{n =>
              lazy val top = n.location.y
              lazy val bottom = top + n.dimensions.h
              lazy val left = n.location.x
              lazy val right = left + n.dimensions.w

              laserLoc.y >= top && laserLoc.y <= bottom && laserLoc.x >= left && laserLoc.x <= right
            }

            if(hits.nonEmpty){
              la.laser.destroy()
              laserAdvancer = None
            }

            hits.foreach{n =>
              n.destroy()
              invaders -= n.id
            }
          }
        } finally {
          playArea.unpauseAnimations()
        }
      }
    }
  }

  def doSvg(): TypedTag[svg.SVG] = {
    import bundle.implicits._
    import bundle.svgAttrs._
    import bundle.svgTags._

    svg(id := ScreenId, width := HRez, height := VRez, viewBox := s"0 0 $HRez $VRez",
      `class` := "playarea",
      defs(),
      rect(x := 0, y := 0, width := HRez, height := VRez, style := "stroke:blue; stroke-width:1px; fill:none;")
    )
  }
}
