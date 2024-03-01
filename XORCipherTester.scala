package hw2

import chisel3._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec

class XORCipherTester extends AnyFlatSpec with ChiselScalatestTester {
  val key  = 0x1234 // Example key
  val data = 0xABCD // Example data
  val width = 16 // Data and key width

  behavior of "XORCipher"
  it should "go through common case (empty -> ready -> encrypted -> decrypted -> empty" in {
    test(new XORCipher(width)).withAnnotations(Seq(WriteVcdAnnotation)) { dut =>

      // empty -> ready
      dut.io.state.expect(CipherState.empty)
      dut.io.in.poke(key)
      dut.io.cmds.clear.poke(false.B)
      dut.io.cmds.loadKey.poke(true.B)
      dut.io.cmds.loadAndEnc.poke(false.B)
      dut.io.cmds.decrypt.poke(false.B)
      dut.clock.step()
      dut.io.out.expect(0.U)


      // ready -> encrypted
      dut.io.state.expect(CipherState.ready)
      dut.io.in.poke(data)
      dut.io.cmds.clear.poke(false.B)
      dut.io.cmds.loadKey.poke(false.B)
      dut.io.cmds.loadAndEnc.poke(true.B)
      dut.io.cmds.decrypt.poke(false.B)
      dut.clock.step()
      dut.io.out.expect((data ^ key).U) // Encrypted data


      // encrypted -> decrypted

      dut.io.state.expect(CipherState.encrypted)
      dut.io.in.poke(0.U) // No new input needed for decryption
      dut.io.cmds.clear.poke(false.B)
      dut.io.cmds.loadKey.poke(false.B)
      dut.io.cmds.loadAndEnc.poke(false.B)
      dut.io.cmds.decrypt.poke(true.B)
      dut.clock.step()
      dut.io.out.expect(data.U) // Decrypted data should be the key



      dut.io.state.expect(CipherState.decrypted)
      dut.io.in.poke(0.U) // No new input needed for decryption
      dut.io.cmds.clear.poke(true.B)
      dut.io.cmds.loadKey.poke(false.B)
      dut.io.cmds.loadAndEnc.poke(false.B)
      dut.io.cmds.decrypt.poke(false.B)
      dut.clock.step()
      dut.io.out.expect(0.U)


    }
  }

  // You will want to create additional tests to cover the other FSM arcs
}
