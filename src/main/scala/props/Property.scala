package props

import scalaz._
import Scalaz._

import metascala.HLists._
import metascala.Nats._
  
sealed trait PropertyError {
  val property: String
}

case class Missing(property: String) extends PropertyError
case class Invalid(property: String) extends PropertyError

trait AttrReader[Src, T] {
  // Failure cases must report a missing property/field name
  def get(e: Src): Result[T]
}

trait AttrWriter[Src, T] {
  def put(t: T, e: Src): Result[Src]
}

// Represents getting a value that can be read from, and written to, a source type.
// This is a type that can be encoded in (and decoded from) Src
// eg. ReadWrite[JSONObject, Person]
trait ReadWrite[Src, T] extends AttrReader[Src,T] with AttrWriter[Src, T] {
  // Helper for working with case classes, should be able to call like this (note the <-> helper from FunctionWs):
  //   case class Foo(...)
  //   someProp >< (Foo <-> Foo.unapply _)
  def ><[U](t2: (T => U, U => T)): ReadWrite[Src, U] = this.xmap(t2._1, t2._2)
}

object ReadWrite {
  def apply[Src, T](getF: Src => Result[T], putF: (T, Src) => Result[Src]) = new ReadWrite[Src, T] {
    def get(e: Src) = getF(e)
    def put(t: T, e: Src) = putF(t, e)
  }
}

// Represents a value that can be read from, and updated from, a source type.
// eg. ReadUpdate[HttpRequest, Person]
// an existing person value can be updated
// with values from the request, or a new person
// could be created.
trait ReadUpdate[Src, T] extends AttrReader[Src, T] with AttrWriter[T, Src] {
  def ><[U](t2: (T => U, U => T)): ReadUpdate[Src, U] = this.xmap(t2._1, t2._2)
}

