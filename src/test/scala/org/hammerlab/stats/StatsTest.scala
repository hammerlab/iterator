package org.hammerlab.stats

import org.hammerlab.test.Suite
import org.scalactic.Equality
import spire.implicits._
import spire.math.Numeric

import scala.math.sqrt

class StatsTest
  extends Suite {

  implicit val de =
    new Equality[Double] {
      override def areEqual(a: Double, b: Any): Boolean =
        b match {
          case d: Double ⇒
            if (a.isNaN && d.isNaN) true
            else a == d
          case _ ⇒
            false
        }
    }

  def check[K : Numeric : Ordering](input: Seq[K],
                                    expected: StatsI[K, Int]): Unit =
    Stats(input) should ===(expected)

  def check[K : Numeric : Ordering](input: Seq[K],
                                    numToSample: Int,
                                    expected: StatsI[K, Int]): Unit =
    Stats(
      input,
      numToSample
    ) should be(
      expected
    )

  def check[K : Numeric : Ordering](input: Seq[K],
                                    numToSample: Int,
                                    onlySampleSorted: Boolean,
                                    expected: StatsI[K, Int]): Unit =
    Stats(
      input,
      numToSample,
      onlySampleSorted
    ) should be(
      expected
    )

  test("empty") {
    check[Int](
      Nil,
      Empty[Int, Int]()
    )
  }

  test("0 to 0") {
    check(
      0 to 0,
      Stats(
        n = 1,
        mean = 0,
        stddev = 0,
        median = 0,
        mad = 0,
        samplesOpt =
          Some(
            Samples(
              1,
              Runs(Seq(0 → 1)),
              Runs(Seq(0 → 1))
            )
          ),
        sortedSamplesOpt = None,
        percentiles = Vector(r"50" → 0.0)
      )
    )
  }

  test("0 to 1") {
    check(
      0 to 1,
      Stats(
        n = 2,
        mean = .5,
        stddev = .5,
        median = .5,
        mad = .5,
        samplesOpt =
          Some(
            Samples(
              2,
              Runs(Seq(0 → 1, 1 → 1)),
              Runs(Seq(0 → 1, 1 → 1))
            )
          ),
        sortedSamplesOpt = None,
        percentiles = Vector(r"50" → .5)
      )
    )
  }

  test("1 to 0") {
    check(
      1 to 0 by -1,
      Stats(
        n = 2,
        mean = .5,
        stddev = .5,
        median = .5,
        mad = .5,
        samplesOpt =
          Some(
            Samples(
              2,
              Runs(Seq(1 → 1, 0 → 1)),
              Runs(Seq(1 → 1, 0 → 1))
            )
          ),
        sortedSamplesOpt =
          Some(
            Samples(
              2,
              Runs(Seq(0 → 1, 1 → 1)),
              Runs(Seq(0 → 1, 1 → 1))
            )
          ),
        percentiles = Vector(r"50" → .5)
      )
    )
  }

  test("0 to 2") {
    check(
      0 to 2,
      Stats(
        n = 3,
        mean = 1,
        stddev = sqrt(2 / 3.0),
        median = 1,
        mad = 1,
        samplesOpt =
          Some(
            Samples(
              3,
              Runs(Seq(0 → 1, 1 → 1, 2 → 1)),
              Runs(Seq(0 → 1, 1 → 1, 2 → 1))
            )
          ),
        sortedSamplesOpt = None,
        percentiles = Vector(r"25" → 0, r"50" → 1, r"75" → 2)
      )
    )
  }

}
