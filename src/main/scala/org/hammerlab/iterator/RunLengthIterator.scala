package org.hammerlab.iterator

import spire.math.Integral
import spire.implicits._

/**
 * Run-length encode an input iterator, replacing contiguous runs of identical elements with pairs consisting of the
 * first element in the run and the number of elements observed.
 *
 * See RunLengthIteratorTest for examples.
 */
class RunLengthIterator[K](val it: BufferedIterator[K]) {
  def runLengthEncode(implicit ord: Ordering[K]): Iterator[(K, Int)] =
    runLengthEncode(ord.equiv(_, _))

  def runLengthEncode(cmpFn: (K, K) => Boolean = (_ == _)): Iterator[(K, Int)] =
    new Iterator[(K, Int)] {
      override def hasNext: Boolean = it.hasNext

      override def next(): (K, Int) = {
        val elem = it.head
        var count = 0
        while (it.hasNext && cmpFn(it.head, elem)) {
          it.next()
          count += 1
        }
        (elem, count)
      }
    }
}

object RunLengthIterator {

  implicit def make[K](it: Iterator[K]): RunLengthIterator[K] = new RunLengthIterator(it.buffered)

  def reencode[K, V: Integral](it: BufferedIterator[(K, V)]): Iterator[(K, V)] =
    new Iterator[(K, V)] {
      override def hasNext: Boolean = it.hasNext

      override def next(): (K, V) = {
        var ret = it.next()
        while (it.hasNext && it.head._1 == ret._1) {
          ret = (ret._1, ret._2 + it.next()._2)
        }
        ret
      }
    }

}
