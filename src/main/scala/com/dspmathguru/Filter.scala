/*
 * Copyright (C) 2015 Richard J. Tobias <dspmathguru@gmail.com>
 *
 * Please read the LICENSE file in the directory structure (MIT)
 *
 * Filter is a generic class for IIR/FIR filters
 *
 * TODO: make type parameter for Filter instead of just Double
 * 
 */

package com.dspmathguru

import breeze.linalg._
import breeze.numerics._
import breeze.math._
import breeze.plot._

class Filter(_b: DenseVector[Double], _a:DenseVector[Double]) {
  private val a = _a
  private val b = _b

  private val ys = DenseVector.zeros[Double](_a.length)
  private val xs = DenseVector.zeros[Double](_b.length)

  private val bDIVa0 = b :/ a(0)
  private val aDIVa0 = 
    if (a.length > 1) a(1, -1) :/ a(0)
    else DenseVector.ones[Double](1)

  def filter(in: DenseVector[Double]): DenseVector[Double] = {
    val rtn = DenseVector.zeros[Double](in.length)
    var i = 0

    for (xN <- in) {
      xs(1 to -1) := xs(0 to -2)
      xs(0) = xN

      val yN = (bDIVa0 dot xs) - { if (ys.length > 1) aDIVa0 dot ys(1 to -1) else 0 } 

      ys(1 to -1) := ys(0 to -2)
      ys(0) = yN

      rtn(i) = yN
      i += 1
    }

    rtn
  }
}