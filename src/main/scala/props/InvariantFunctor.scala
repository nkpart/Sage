package props

import scalaz._
import Scalaz._

trait InvariantFunctor[M[_]] {
  def xmap[A,B](ma: M[A], f: A => B, g: B => A): M[B]
}