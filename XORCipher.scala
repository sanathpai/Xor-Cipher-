package hw2

import chisel3._
import chisel3.util._


object CipherState extends ChiselEnum {
  val empty, ready, encrypted, decrypted = Value
}

class XORCipherCmds extends Bundle {
  val clear      = Input(Bool())
  val loadKey    = Input(Bool())
  val loadAndEnc = Input(Bool())
  val decrypt    = Input(Bool())
}


/**
  * @param width :    Int
  * @field in:        UInt           (Input) - payload or key
  * @field cmds:      XORCipherCmds  (Input)
  * @field out:       UInt           (Output)
  * @field full:      Bool           (Output)
  * @field encrypted: Bool           (Output)
  * @field state:     CipherState    (Output) - visible for testing
  */
class XORCipherIO(width: Int) extends Bundle {
  // Define input and output signals
//  val in = Input(UInt(width.W))
//  val out = Output(UInt(width.W))
//  val cmds = Input(new XORCipherCmds)
//  val state= Output(CipherState())
//  val full= Output(Bool())
  val in = Input(UInt(width.W))
  val out = Output(UInt(width.W))
  val cmds = Input(new XORCipherCmds)
  val state = Output(CipherState())
  val full = Output(Bool())
  val encrypted = Output(Bool())

}


/**
  * @param width Int
  */
class XORCipher(width: Int) extends Module {
  val io = IO(new XORCipherIO(width))
  val data = RegInit(0.U(width.W))
  val key = RegInit(0.U(width.W))
  // State register
  val state = RegInit(CipherState.empty)



  // State machine logic
  switch(state) {
    is(CipherState.empty) {
      when(io.cmds.loadKey) {
        // Load key
        key := io.in
        state := CipherState.ready
      }.otherwise {
        data:=0.U
        key:=0.U
        state:=CipherState.empty
      }
    }

    is(CipherState.ready) {
      when(io.cmds.clear) {
        // Encrypt and store data
        data := 0.U
        key:=0.U
        state := CipherState.empty
      }.elsewhen(io.cmds.loadKey) {
        // Decrypt data
        key:=io.in
        data:=0.U
        state := CipherState.ready
      }.elsewhen(io.cmds.loadAndEnc){
        data:=io.in ^key
        state:=CipherState.encrypted
      }

    }
    is(CipherState.decrypted) {
      when(io.cmds.clear) {
        // Encrypt and store data
        data := 0.U
        key:=0.U
        state := CipherState.empty
      }.elsewhen(io.cmds.loadAndEnc) {
        // Decrypt data
        data := io.in^key
        state := CipherState.encrypted
      }.elsewhen(!io.cmds.loadAndEnc){
        data:=0.U
        state:=CipherState.ready
      }
    }
    is(CipherState.encrypted) {
      when(io.cmds.clear) {
        // Encrypt and store data
        data := 0.U
        key:=0.U
        state := CipherState.empty
      }.elsewhen(io.cmds.loadAndEnc) {
        // Decrypt data
        data := io.in ^ key
        state := CipherState.encrypted
      }.elsewhen(io.cmds.decrypt){
        data:= data ^ key
        state:=CipherState.decrypted
      }
    }

  }

  // Provide state as CipherState
  io.state := state
  // Output logic
  io.out := data
  io.full := (state === CipherState.encrypted) || (state === CipherState.decrypted)
  io.encrypted := state === CipherState.encrypted
}
