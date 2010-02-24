package sage

import hprops._
import scalaz._
import Scalaz._
import com.google.appengine.api.datastore._
import scala.collection.JavaConversions._

trait EntityBase[T] {
  val kind: String
  def * : ReadWrite[Entity, T]
  
  def <<(t: T)(implicit ds: DatastoreService): Keyed[T] = {
    val e = freshEntity(t)
    Keyed(ds.put(e), t)
  }
  
  def parentedSave(t: T, parent: Key)(implicit ds: DatastoreService): Keyed[T] = {
    val e = parentedEntity(t, parent)
    Keyed(ds.put(e), t)
  }
  
  def <<(kt: Keyed[T])(implicit ds: DatastoreService): Keyed[T] = {
    val e = write(kt.value, new Entity(kt.key))
    Keyed(ds.put(e), kt.value)
  }
  
  def <<++(ts: Seq[T])(implicit ds: DatastoreService): Iterable[Keyed[T]] = {
    val es = ts map freshEntity
    val keys: Iterable[Key] = ds.put(asIterable(es))
    keys zip ts map ((k:Key, b:T) => Keyed(k,b)).tupled
  }
  
  def lookup(id: Long)(implicit ds: DatastoreService): Option[Keyed[T]] = {
    val got = (() => ds.get(KeyFactory.createKey(kind, id))).throws.success
    for (entity <- got; t <- read(entity)) yield (Keyed(entity.getKey, t))
  }
  
  def childrenOf(pk: Key)(implicit ds: DatastoreService): Iterable[Keyed[T]] = {
    find.query(qry => qry.setAncestor(pk)).iterable
  }
  
  def find: Find[T] = Find(this)
  
  def write(t: T, e: Entity): Entity = this.*.put(t, e).success.get
  def read(m: Entity): Option[T] = this.*.get(m).success
  
  def keyedEntity(t: T, key: Key): Entity = write(t, new Entity(key))
  def freshEntity(t: T) = write(t, new Entity(kind))  
  def parentedEntity(t: T, parentKey: Key) = write(t, new Entity(kind, parentKey))  
}

abstract class Base[T](val kind: String) extends EntityBase[T]
