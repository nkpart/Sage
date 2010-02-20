package sage
package http

import scalaz.http.request._
import scalaz._
import Scalaz._

trait StringW {
  val str: String
  
  import props._
  def as[T](implicit p: Postable[T]) = props.Property[Request[Stream], T](
    r => (r |! str).map(_.mkString).toSuccess(missing(str).wrapNel) >>= (v => p.read(v).toSuccess(invalid(str).wrapNel)),
    (t, r) => error("cannot write to requests")
  )
}

trait Postable[T] {
  def read(str: String): Option[T]
}

object Postable {
  def postable[T](f: String => Option[T]) = new Postable[T] { def read(str: String) = f(str) }
  
  implicit val string = postable[String](s => some(s))
  implicit val int = postable[Int](s => s.parseInt.success)
}

object StringW {
  implicit def to(s: String) = new StringW { val str = s }
  implicit def from(sw: StringW) = sw.str
}

