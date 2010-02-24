package sage
package http

import hprops._
import scalaz.http.request._
import scalaz._
import Scalaz._

import metascala.HLists._

trait StringW {
  val str: String
  
  import hprops._
  
  def as[T](implicit p: Postable[T]) = new ReadUpdate[Request[Stream], T] {
    def get(r: Request[Stream]) = (r |! str).map(_.mkString).toSuccess(missing(str).wrapNel) >>= (v => p.read(v).toSuccess(invalid(str).wrapNel))
    
    def put(r: Request[Stream], t: T) = get(r).fail.map(_.list) >>= {
      case Missing(_) :: Nil => success(t).fail
      case a => failure(a.toNel.get).fail
    } validation
  }
}

trait Postable[T] {
  def read(str: String): Option[T]
}

object Postable {
  def postable[T](f: String => Option[T]) = new Postable[T] { def read(str: String) = f(str) }
  
  implicit val string = postable[String](some _)
  implicit val int = postable[Int](_.parseInt.success)
  implicit val long = postable[Long](_.parseLong.success)
  implicit val double = postable[Double](_.parseDouble.success)
  
  def test {
    import StringW._
    case class Foo(bar: String, baz: String)
  }
}

object StringW {
  implicit def to(s: String) = new StringW { val str = s }
  implicit def from(sw: StringW) = sw.str
}

