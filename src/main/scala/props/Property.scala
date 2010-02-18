package props

import scalaz._
import Scalaz._

import metascala.HLists._
import metascala.Nats._
  
trait AttrReader[Src, T] {
  def get(e: Src): Validation[NonEmptyList[String], T]
}

trait AttrWriter[Src, T] {
  def put(t: T, e: Src): Src
}

// Represents getting a value that can be read from, and written to, a source type.
trait Property[Src, T] extends AttrReader[Src,T] with AttrWriter[Src, T] {
  // Failure cases must report a missing property/field name
  def get(e: Src): Validation[NonEmptyList[String], T]
  
  def put(t: T, e: Src): Src
  
  // Helper for working with case classes, should be able to call like this (note the <-> helper):
  //   case class Foo(s: string)
  //   someProp >< (Foo <-> Foo.unapply _)
  def ><[U](t2: (T => U, U => T)): Property[Src, U] = this.xmap(t2._1, t2._2)
}

object Property {
  def apply[Src, T](getF: Src => Validation[NonEmptyList[String], T], putF: (T, Src) => Src) = new Property[Src, T] {
    def get(e: Src) = getF(e)
    def put(t: T, e: Src) = putF(t, e)
  }
}
