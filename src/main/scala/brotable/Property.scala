package brotable

import scalaz._
import Scalaz._
import com.google.appengine.api.datastore._


trait Property[T] {
  def get(e: Entity): Option[T]
  def put(t: T, e: Entity): Entity
}

case class MappedProperty[T, P](f: P => T, g: T => Option[P], parent: Property[P]) extends Property[T] {
  def get(e: Entity): Option[T] = parent.get(e) map f
  def put(t: T, e: Entity) = parent.put(g(t).get, e)
}

trait P1[T] extends Property[T] {  
  def ~[U](prop: P1[U]) = new P2[T,U] {
    def get(e: Entity) = P1.this.get(e) <|*|> prop.get(e)
      
    def put(t2: (T,U), e: Entity) = 
        prop.put(t2._2, P1.this.put(t2._1, e))
  }
  
  def <>[V](f: (T => V), g: (V => Some[T])) = MappedProperty[V, T](f,g, this)
}

trait P2[T,U] extends Property[(T,U)] {
  def <>[V](f: ((T,U) => V), g: (V => Some[(T,U)])) = MappedProperty[V, (T,U)](f.tupled,g, this)
}
