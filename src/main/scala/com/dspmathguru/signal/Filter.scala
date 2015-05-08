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

package com.dspmathguru.signal

import breeze.linalg._
import breeze.numerics._
import breeze.math._
import breeze.signal._
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
      xs(1 to -1) := xs(0 to -2).toDenseVector  // need a copy otherwise this doesn't work
      xs(0) = xN
      val yN = (bDIVa0 dot xs) - { if (ys.length > 1) aDIVa0 dot ys(1 to -1) else 0 } 
      if (ys.length > 1) ys(1 to -1) := ys(0 to -2).toDenseVector  // need a copy
      ys(0) = yN

      rtn(i) = yN

      i += 1
    }

    rtn
  }
  
  def postpad(x: DenseVector[Double], N: Int): DenseVector[Double] = {
    val rtn = DenseVector.zeros[Double](N)
    rtn(0 to x.length) := x
    
    rtn
  }
  
  /**
   * Frequency response of the filter
   * 
   * freqz is similar to how it is used in Matlab/Octave
   * 
   * @param _N the number of points
   * @param _Fs the sampling frequency, if <= 0.0 the use omega
   * @param _whole true when use whole region, false use half
   */
  def freqz(_N: Int = 512, _Fs: Double = 0.0, _whole: Boolean = false)
    : (DenseVector[Double], DenseVector[Double]) = {
    val k = max(a.length, b.length)
    
    val n:Int =
      if (k > _N/2) _N*(pow(2.0, ceil(log2(2.0*(k.toDouble/_N.toDouble))))).toInt
      else _N
      
    val NN = if (_whole) 2*n else n
    
    val Fs = if (_Fs <= 0.0) 1.0 else _Fs
    
    val padSize = NN*(ceil(k.toDouble/NN.toDouble).toInt)
    val _a = postpad(a, padSize)
    val _b = postpad(b, padSize)
    
    val f = Fs * DenseVector.tabulate(NN) { i => i.toDouble } :/ NN.toDouble
    
    val hb = DenseVector.zeros[Double](n)
    val ha = DenseVector.zeros[Double](n)
    
    for (i <- 0 until NN) {
      val Hb = abs(fourierTr(postpad(_b(i to i+NN-1), NN)))
      hb := hb + abs(Hb(0 to n))
      val Ha = abs(fourierTr(postpad(_a(i to i+NN-1), NN)))
      ha := ha + Ha(0 to n)
    } 
    
    val H = hb :/ ha
    
    (H, f)
  }
}
