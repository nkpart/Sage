package sage

import scalaz._
import Scalaz._
import com.google.appengine.api.datastore._

trait Property[T, V] {
  def get(e: V): Validation[NonEmptyList[String], T]
  def put(t: T, e: V): Entity
}

case class MappedProperty[T, P](f: P => T, g: T => Option[P], parent: Property[P, Entity]) extends Property[T, Entity] {
  def get(e: Entity): Validation[NonEmptyList[String], T] = parent.get(e) map f
  def put(t: T, e: Entity) = parent.put(g(t).get, e)
}

trait P1[A] extends Property[A, Entity] {  
  def <>[Z](f: (A => Z), g: (Z => Some[A])) = MappedProperty[Z, A](f, g, this)
  
  def ~[B](prop: P1[B]) = new P2[A,B] {
    def get(e: Entity) = P1.this.get(e) <|*|> prop.get(e)
    
    def put(t2: (A, B), e: Entity) = prop.put(t2._2, P1.this.put(t2._1, e))
  }
}

trait P2[A, B] extends Property[(A, B), Entity] {
  def <>[Z](f: ((A, B) => Z), g: (Z => Some[(A, B)])) = MappedProperty[Z, (A,B)](f.tupled, g, this)
}

trait P3[A, B, C] extends Property[(A, B, C), Entity] {
  def <>[Z](f: ((A, B, C) => Z), g: (Z => Some[(A, B, C)])) = MappedProperty[Z, (A, B, C)](f.tupled, g, this)
}


