package hw2

import chisel3._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec

class SineWaveGenTester extends AnyFlatSpec with ChiselScalatestTester {
  def testSineWaveGen(sw: SineWave, stride: Int): Unit = {
    test(new SineWaveGen(sw)) { dut =>
      // Drive inputs
      dut.io.en.poke(true.B)
      dut.io.stride.poke(stride.U)

      for (i <- 0 until sw.period by stride) {
        val expectedOutput = sw(i) // Calculate expected output using apply function
        dut.io.out.expect(expectedOutput.S) // Assert that the output matches the expected value
        dut.clock.step() // Advance the clock to generate the next output

      }
    }
  }

  behavior of "SineWaveGen"
  it should "correctly calculate output for period=16 stride=1" in {
    testSineWaveGen(new SineWave(16, 128), 1)
  }

  it should "correctly calculate output for period=16 stride=2" in {
    testSineWaveGen(new SineWave(16, 128), 2)
  }

  it should "correctly calculate output for period=16 stride=3" in {
    testSineWaveGen(new SineWave(16, 128), 3)
  }

  it should "correctly calculate output for period=10 stride=1" in {
    testSineWaveGen(new SineWave(10, 128), 1)
  }

  it should "correctly calculate output for period=10 stride=3" in {
    testSineWaveGen(new SineWave(10, 128), 3)
  }
}
