package hw2

import chisel3._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec
import scala.util.Random


class PolyEvalTester extends AnyFlatSpec with ChiselScalatestTester {
  val width = 8

  def testPolyEvalOut(n: Int): Unit = {
    val coefs = Seq.fill(n + 1)(Random.nextInt(128))
    test(new PolyEval(coefs, width)) { dut =>
      val y = Random.nextInt(256)
      dut.io.x.poke(y)

      val expectedOut = coefs.zipWithIndex.map { case (coef, i) =>
        (BigInt(coef) * BigInt(y).pow(i))
      }.sum

      dut.io.out.expect(expectedOut.U)
    }
  }

  behavior of "PolyEval"
  it should "correctly calculate output for deg(2) poly" in {
    testPolyEvalOut(2)
  }

  it should "correctly calculate output for deg(3) poly" in {
    testPolyEvalOut(3)
  }

  it should "correctly calculate output for deg(4) poly" in {
    testPolyEvalOut(4)
  }

  it should "correctly calculate output for deg(5) poly" in {
    testPolyEvalOut(5)
  }
}