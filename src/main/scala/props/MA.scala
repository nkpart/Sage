package props

import scalaz._
import Scalaz._

trait MA[M[_], A] {
  val ma: M[A]
  
  def xmap[B](f: A => B, g: B => A)(implicit xf: InvariantFunctor[M]) = xf.xmap(ma, f, g)
}

trait MAs {
  def ma[M[_], A](v: M[A]) = new MA[M, A] {
    val ma = v
  }
  
  implicit def anyMA[M[_], T](mt: M[T]) = ma[M, T](mt)
  implicit def propertyMA[Src, T](p: Property[Src,T]) = ma[PartialApply1Of2[Property, Src]#Apply, T](p)
}