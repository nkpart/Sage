package brotable

import scalaz._
import Scalaz._
import com.google.appengine.api.datastore._

trait EntityBase[T] {
  val kind: String
  def * : Property[T]
  
  def insert(t: T)(implicit ds: DatastoreService): (Key, T) = {
    val e = new Entity(kind)
    write(t, e)
    (ds.put(e), t)
  }
  
  def save(key: Key, t: T)(implicit ds: DatastoreService): (Key, T) = {
    val e = new Entity(key)
    write(t,e)
    (ds.put(e), t)
  }
  
  def lookup(id: Long)(implicit ds: DatastoreService): Option[T] = {
    (() => ds.get(KeyFactory.createKey(kind, id))).throws.success >>= { e =>
      read(e)
    }
  }
  
  def write(t: T, e: Entity): Entity = this.* put (t, e)
  def read(m: Entity): Option[T] = this.* get(m)
}

abstract class Base[T](val kind: String) extends EntityBase[T]
