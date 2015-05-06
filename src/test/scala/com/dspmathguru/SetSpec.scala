package com.dspmathguru

import org.scalatest.FlatSpec
import breeze.linalg._

class SetSpec extends FlatSpec {
  "Filter b coefficents all 0s" should "stay 0" in {
    val b = DenseVector.zeros[Double](10)
    val a = DenseVector.ones[Double](1)
    val f = new Filter(b, a)
    
    val in = DenseVector.rand[Double](1000)
    
    val out = f.filter(in)
    
    assert(sum(out) == 0.0)
  }
}