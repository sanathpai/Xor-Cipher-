package hw2

import chisel3._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec

class ComplexALUTester extends AnyFlatSpec with ChiselScalatestTester {
  behavior of "ComplexALU"
  it should "correctly calculate realOut onlyAdd=true" in {
    test(new ComplexALU(width = 5, onlyAdder = true)) { dut =>
      dut.io.c0.real.poke(4.S)
      dut.io.c0.imag.poke(3.S)
      dut.io.c1.real.poke(2.S)
      dut.io.c1.imag.poke(1.S)
      dut.io.out.real.expect(6.S)
    }
  }

  it should "correctly calculate realOut onlyAdd=false" in {
    test(new ComplexALU(width=5, onlyAdder=false)) { dut =>
      dut.io.c0.real.poke(4.S)
      dut.io.c0.imag.poke(3.S)
      dut.io.c1.real.poke(2.S)
      dut.io.c1.imag.poke(1.S)

      // Test addition
      dut.io.doAdd.get.poke(true.B)
      dut.clock.step()
      dut.io.out.real.expect(6.S)  // Expected output for addition (4 + 2)

      // Test subtraction
      dut.io.doAdd.get.poke(false.B)
      dut.clock.step()
      dut.io.out.real.expect(2.S)  // Expected output for subtraction (4 - 2)
    }
  }

  it should "correctly calculate imagOut onlyAdd=true" in {
    test(new ComplexALU(width=5, onlyAdder=true)) { dut =>
      // Poke input values
      dut.io.c0.real.poke(4.S)
      dut.io.c0.imag.poke(3.S)
      dut.io.c1.real.poke(2.S)
      dut.io.c1.imag.poke(1.S)

      dut.clock.step()  // Perform addition (onlyAdder = true)
      dut.io.out.imag.expect(4.S)  // Expected output for addition (3 + 1)
    }
  }

  it should "correctly calculate imagOut onlyAdd=false" in {
    test(new ComplexALU(width=5, onlyAdder=false)) { dut =>
      dut.io.c0.real.poke(4.S)
      dut.io.c0.imag.poke(3.S)
      dut.io.c1.real.poke(2.S)
      dut.io.c1.imag.poke(1.S)

      // Test addition
      dut.io.doAdd.get.poke(true.B)
      dut.clock.step()
      dut.io.out.imag.expect(4.S)  // Expected output for addition (3 + 1)

      // Test subtraction
      dut.io.doAdd.get.poke(false.B)
      dut.clock.step()
      dut.io.out.imag.expect(2.S)  // Expected output for subtraction (3 - 1)
    }
  }
}
