package com.dspmathguru.signal

case class Precision(val p:Double)

object Precision {
  class withAlmostEquals(d: Double) {
      def ~=(d2: Double) (implicit p: Precision) = (d-d2).abs <= p.p
  }  

  implicit def add_~=(d: Double) = new withAlmostEquals(d)
}
