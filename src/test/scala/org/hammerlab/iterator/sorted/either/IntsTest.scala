package org.hammerlab.iterator.sorted.either

import org.hammerlab.iterator.sorted
import org.hammerlab.iterator.sorted.IdentityIntConversions

class IntsTest
  extends Suite
    with sorted.IntsTest
    with IdentityIntConversions {

  override def expected: Map[String, Seq[Either[Int, Int]]] =
    Map(
      "1,2,3 4,5,6" →
        Seq(
          L(1),
          L(2),
          L(3),
          R(4),
          R(5),
          R(6)
        ),
      "1,3,5 2,4,6" →
        Seq(
          L(1),
          R(2),
          L(3),
          R(4),
          L(5),
          R(6)
        ),
      "1,2,3 1,2,3" →
        Seq(
          L(1),
          R(1),
          L(2),
          R(2),
          L(3),
          R(3)
        ),
      "1,2,4,7,9 1,3,5,6,7,8" →
        Seq(
          L(1),
          R(1),
          L(2),
          R(3),
          L(4),
          R(5),
          R(6),
          L(7),
          R(7),
          R(8),
          L(9)
        ),
      "empty empty" → Nil,
      "empty 1" → Seq(R(1)),
      "empty 1,10,100" →
        Seq(
          R(1),
          R(10),
          R(100)
        ),
      "1 empty" → Seq(L(1)),
      "1,10,100 empty" →
        Seq(
          L(1),
          L(10),
          L(100)
        )
    )
}

