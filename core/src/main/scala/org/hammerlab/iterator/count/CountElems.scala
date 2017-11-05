package org.hammerlab.iterator.count

import hammerlab.iterator.macros.IteratorOps

import scala.collection.mutable

@IteratorOps
class CountElems[T](it: Iterator[T]) {
  def countElems: Map[T, Int] = {
    val counts = mutable.Map[T, Int]()
    it.foreach {
      elem ⇒
        counts.update(
          elem,
          counts.getOrElse(elem, 0) + 1
        )
    }
    counts.toMap
  }
}
