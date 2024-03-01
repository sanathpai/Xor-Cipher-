package hw2

import chisel3._
import Numeric.BigIntIsIntegral._
import scala.collection.mutable.ArrayBuffer

class PolyEval(coefs: Seq[Int], width: Int) extends Module {
  val io = IO(new Bundle {
    val x = Input(UInt(width.W))
    val out = Output(UInt())
  })

  val powersOfXArray = ArrayBuffer.fill(coefs.length)(0.U(width.W))
  powersOfXArray(0) = 1.U

  // Compute powers of x
  for (i <- 1 until coefs.length) {
    powersOfXArray(i) = powersOfXArray(i - 1) * io.x
  }

  // Multiply powers of x with corresponding coefficients and accumulate
  val termResults = powersOfXArray.zip(coefs).map { case (power, coef) =>
    power * coef.U
  }

  // Sum up the term results to get the final output
  io.out := termResults.reduce((acc, term) => acc +& term)
}
