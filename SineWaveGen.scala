package hw2

import chisel3._
import chisel3.util.log2Ceil


class SineWave(val period: Int, val amplitude: Int) {
  require(period > 0)
  val B: Double = (2.0 * math.Pi) / period.toDouble

  def apply(index: Int): Int = (amplitude.toDouble * math.sin(B * index)).toInt
}


/**
  *
  * @param s : SineWave (internally contains period & amplitude)
  * ________________________________
  * @field stride:  UInt      (Input)
  * @field en:      Bool      (Input)
  * @field out:     SInt      (Output)
  */
class SineWaveGenIO (sw: SineWave) extends Bundle {
  val stride: UInt = Input(UInt(log2Ceil(sw.period + 1).W))
  val en: Bool = Input(Bool())
  val out: SInt = Output(SInt(sw.amplitude.W))
}


/**
  * 
  * @param s : SineWave (internally contains period)
  */
class SineWaveGen(sw: SineWave) extends Module {
  val io = IO(new SineWaveGenIO(sw))

  val ptr = RegInit(0.U(log2Ceil(sw.period).W)) // Register for pointer

  val table = VecInit(Seq.tabulate(sw.period)(i => sw.apply(i).S))

  io.out:=0.S
  when (io.en) {
    io.out := table(ptr) // Access values from Vec
    ptr := (ptr + io.stride) % sw.period.U
  }
  }
