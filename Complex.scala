package hw2

import chisel3._

// Note ??? will compile but not work at runtime.

/**
  * @param width : Int
  * ___________________
  * @field real:  SInt
  * @field imag:  SInt
  * @method def sumReal(that:  ComplexNum): SInt 
  * @method def sumImag(that:  ComplexNum): SInt 
  * @method def diffReal(that: ComplexNum): SInt
  * @method def diffImag(that: ComplexNum): SInt
  */
class ComplexNum(width: Int) extends Bundle {
  val real = SInt(width.W)
  val imag = SInt(width.W)

  def sumReal(that: ComplexNum): SInt = {
      real +& that.real
  }
  def sumImag(that:ComplexNum): SInt = {
      imag +& that.imag
  }
  def diffReal(that:ComplexNum): SInt = {
    real -& that.real
  }
  def diffImag(that:ComplexNum): SInt = {
    imag -& that.imag
  }

}


/**
  * @param width     : Int
  * @param onlyAdder : Boolean
  * ___________________
  * @field doAdd: Option[Bool]  (Input)
  * @field c0:  ComplexNum      (Input)
  * @field c1:  ComplexNum      (Input)
  * @field out: ComplexNum      (Output)
  */
class ComplexALUIO(width: Int, onlyAdder: Boolean) extends Bundle {
  val doAdd:Option[Bool] = if (onlyAdder) None else Some(Input(Bool()))
  val c0 = Input(new ComplexNum(width))
  val c1 = Input(new ComplexNum(width))
  val out = Output(new ComplexNum(width+1))
}


/**
  * @param width     : Int
  * @param onlyAdder : Boolean
  */
class ComplexALU(width: Int, onlyAdder: Boolean) extends Module {
  val io = IO(new ComplexALUIO(width, onlyAdder))
  val realOperation = if (onlyAdder) {
    io.c0.real +& io.c1.real  // Simple addition for adder-only mode
  } else {
    Mux(io.doAdd.get, io.c0.sumReal(io.c1), io.c0.diffReal(io.c1))
  }
  val imagOperation = if (onlyAdder) {
    io.c0.imag +& io.c1.imag
  } else {
    Mux(io.doAdd.get, io.c0.sumImag(io.c1), io.c0.diffImag(io.c1))
  }

  io.out.real := realOperation
  io.out.imag := imagOperation
}
