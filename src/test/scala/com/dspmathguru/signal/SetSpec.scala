package com.dspmathguru.signal

import org.scalatest.FlatSpec
import breeze.linalg._
import breeze.numerics._
import breeze.plot._

class SetSpec extends FlatSpec {
  val N = 10
  val M = 30
  val prec = 0.00001
  implicit val precision = Precision(prec)
  
  "Filter b coefficents all 0s" should "stay 0" in {
    val b = DenseVector.zeros[Double](10)
    val a = DenseVector.ones[Double](1)
    val f = new Filter(b, a)
    
    val in = DenseVector.rand[Double](M)
    
    val out = f.filter(in)
    
    assert(sum(out) == 0.0)
  }
  
  s"Filter delay by $N" should "be delayed" in {
    val b = DenseVector.zeros[Double](2*N)
    b(N-1) = 1.0  // The delay
    
    val a = DenseVector.ones[Double](1)
    val f = new Filter(b, a)
    
    val in = DenseVector.rand[Double](M)
    
    val out = f.filter(in)
    
    assert(sum(abs(out(N-1 to -1) - in(0 to -N))) < prec)
  }
  
  "Freqz of butterworth" should "be near 0" in {
    val b = DenseVector[Double](0.292893218813452, 0.585786437626905, 0.292893218813452)
    val a = DenseVector[Double](1.0, 0.0, 0.171572875253810)
    
    val f = new Filter(b, a)
    
    val rtn = f.freqz()
    
    val fig = Figure()
    val p = fig.subplot(0)
    val x = rtn._2
    val H = rtn._1
    
    p += plot(x, H)
    
    p.xlabel = "omega - radians"
    p.ylabel = "magnitude" 
  }
}
