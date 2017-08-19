package org.hammerlab.iterator.scan

import cats.Monoid
import org.hammerlab.iterator.DropRightIterator._

case class ScanIterator[T](it: Iterator[T]) {

  def scanL(implicit m: Monoid[T]): Iterator[T] = scanL(includeCurrentValue = false)
  def scanLeftInclusive(implicit m: Monoid[T]): Iterator[T] = scanL(includeCurrentValue = true)

  def scanL(includeCurrentValue: Boolean)(
      implicit m: Monoid[T]
  ): Iterator[T] = {
    val scanned =
      it
        .scanLeft(
          m.empty
        )(
          m.combine
        )

    if (includeCurrentValue)
      scanned.drop(1)
    else
      scanned.dropRight(1)
  }

  def scanR(implicit m: Monoid[T]): Iterator[T] = scanR(includeCurrentValue = false)
  def scanRightInclusive(implicit m: Monoid[T]): Iterator[T] = scanR(includeCurrentValue = true)

  def scanR(includeCurrentValue: Boolean)(
      implicit m: Monoid[T]
  ): Iterator[T] = {
    val scanned =
      it
        .scanRight(
          m.empty
        )(
          m.combine
        )

    if (includeCurrentValue)
      scanned.dropRight(1)
    else
      scanned.drop(1)
  }
}

object ScanIterator {
  implicit def makeScanIterator[T](it: Iterator[T]): ScanIterator[T] = ScanIterator(it)
  implicit def makeScanIterator[T](it: Iterable[T]): ScanIterator[T] = ScanIterator(it.iterator)
  implicit def makeScanIterator[T](it: Array[T]): ScanIterator[T] = ScanIterator(it.iterator)
}