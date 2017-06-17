package com.lilypepper.nvaders

import com.lilypepper.nvaders.NVaders._
import SvgDefinitions._

object Barrier {
  val BarrierAnimationSeq = AnimationSeq(Seq(BarrierId))
  val BarrierDimensions = Size(1,1)

  type XDim = Int
  type YDim = Int

  var pixels:Map[YDim, Map[XDim, BarrierPixel]] = Map()

  case class BarrierPixel(var location: Point) extends Widget {
    override val animationSeq = BarrierAnimationSeq
    override val dimensions = BarrierDimensions
  }

  def drawBarriers(y: YDim): Unit = {
    for(i <- Seq(
      HRez / 2 - 7 * 23 / 2 ,
      HRez / 2 - 3 * 23 / 2 ,
      HRez / 2 + 23 / 2,
      HRez / 2 + 5 * 23 / 2)) {
      Barrier.drawBarrier(i, y)
    }
  }

  private def drawBarrier(x: XDim, y: YDim): Unit ={
    val rows = Seq(
      (0, 4 to 18),
      (1, 3 to 19),
      (2, 2 to 20),
      (3, 1 to 21),
      (4, 0 to 22),
      (5, 0 to 22),
      (6, 0 to 22),
      (7, 0 to 22),
      (8, 0 to 22),
      (9, 0 to 22),
      (10, 0 to 22),
      (11, 0 to 22),
      (12, 0 to 7),
      (12, 15 to 22),
      (13, 0 to 6),
      (13, 16 to 22),
      (14, 0 to 5),
      (14, 17 to 22),
      (15, 0 to 5),
      (15, 17 to 22)
    )

    for{
      (y2, x2s) <- rows
      x2 <- x2s
    }{
      val xr = x + x2
      val yr = y + y2

      val pixel = BarrierPixel(Point(xr, yr))
      pixel.updateView()

      pixels += yr -> (pixels.getOrElse(yr, Map()) + (xr -> pixel))
    }
  }
}
