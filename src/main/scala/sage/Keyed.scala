package sage

import com.google.appengine.api.datastore._
import scalaz._
import Scalaz._

trait Keyed[T] {
  val key: Key
  val value: T
  
  // TODO: move this onto Query
//  def children[T](ds: DatastoreService)(implicit ec: EntityCreatable[T], k: Kind[T]): Iterable[Keyed[T]] = {
//    Find[T].query(_.setAncestor(this.key)).iterable(ds)
//  }
}

object Keyed {
  def apply[T](k: Key, t: T) = new Keyed[T] { val value = t; val key = k}
}
