package sage
package http

import scalaz.http.request._
import scalaz._
import Scalaz._

trait StringW {
  val str: String
  
  def as[T](implicit p: Postable[T]) = props.Property[Request[Stream], T](
    r => ((r |! str) map (_.mkString) >>= (p.read _)).toSuccess(str.wrapNel),
    (t, r) => error("cannot write to requests")
  )
}

trait Postable[T] {
  def read(str: String): Option[T]
}

object Postable {
  implicit val string = postable[String](s => some(s))
  def postable[T](f: String => Option[T]) = new Postable[T] { def read(str: String) = f(str) }
}

object StringW {
  implicit def to(s: String) = new StringW { val str = s }
  implicit def from(sw: StringW) = sw.str
}

