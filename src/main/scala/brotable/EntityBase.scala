package sage

import scalaz._
import Scalaz._
import com.google.appengine.api.datastore._
import scala.collection.JavaConversions._

trait EntityBase[T] {
  val kind: String
  def * : Property[T]
  
  def <<(t: T)(implicit ds: DatastoreService): (Key, T) = {
    val e = asEntity(t)
    (ds.put(e), t)
  }
  
  def <<++(ts: Seq[T])(implicit ds: DatastoreService): Iterable[(Key,T)] = {
    val es = ts map asEntity
    val keys: Iterable[Key] = ds.put(asIterable(es))
    keys zip ts
  }
  
  def lookup(id: Long)(implicit ds: DatastoreService): Option[T] = {
    (() => ds.get(KeyFactory.createKey(kind, id))).throws.success >>= { e =>
      read(e)
    }
  }
  
  def find: Find[T] = Find(this)
  
  def write(t: T, e: Entity): Entity = this.* put (t, e)
  def read(m: Entity): Option[T] = this.* get (m)
  
  def asEntity(t: T) = {
    val e = new Entity(kind)
    write(t, e)
    e
  }
}

abstract class Base[T](val kind: String) extends EntityBase[T]
