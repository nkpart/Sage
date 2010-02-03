package sage

import com.google.appengine.api.datastore._
import scalaz._
import Scalaz._

case class Keyed[T](
  key: Key,
  value: T
) {
  
  // TODO: move this onto Query
//  def children[T](ds: DatastoreService)(implicit ec: EntityCreatable[T], k: Kind[T]): Iterable[Keyed[T]] = {
//    Find[T].query(_.setAncestor(this.key)).iterable(ds)
//  }
}

