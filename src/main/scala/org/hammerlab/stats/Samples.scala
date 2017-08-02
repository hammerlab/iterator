package org.hammerlab.stats

import cats.Show
import cats.Show.show
import cats.implicits._
import spire.implicits._
import spire.math.Integral

/**
 * Used by [[Stats]] to wrap some [[Runs]] of elements from the start and end of a dataset.
 * @param n total number of elements in the dataset.
 * @param first [[Runs]] of elements from the start of the dataset.
 * @param last [[Runs]] of elements from the end of the dataset.
 * @tparam K arbitrary element type
 * @tparam V [[Integral]] type, e.g. [[Int]] or [[Long]].
 */
case class Samples[K, V: Integral](n: V,
                                   first: Runs[K, V],
                                   last: Runs[K, V]) {
  def isEmpty: Boolean = first.isEmpty
  def nonEmpty: Boolean = first.nonEmpty
}

object Samples {
  implicit def makeShow[K, V: Integral](implicit showRuns: Show[Runs[K, V]]): Show[Samples[K, V]] =
    show {
      case Samples(n, first, last) ⇒
        val numSampled = first.num + last.num
        val numSkipped = n - numSampled
        if (numSkipped > 0)
          s"${first.show}, …, ${last.show}"
        else
          removeOverlap(-numSkipped, first, last).show
    }

  def removeOverlap[K, V: Integral](num: V,
                                    first: Runs[K, V],
                                    last: Runs[K, V]): Runs[K, V] = {
    val lastIt = last.iterator.buffered
    var dropped = Integral[V].zero
    Runs(
      first ++
        lastIt
          .dropWhile {
            t ⇒
              val (_, count) = t
              val drop = dropped < num
              dropped += count
              drop
          }
    )
  }
}
