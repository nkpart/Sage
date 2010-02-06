package sage

import scalaz._
import Scalaz._
import com.google.appengine.api.datastore._

trait Property[T, V] {
  def get(e: V): Validation[NonEmptyList[String], T]
  def put(t: T, e: V): V
}

case class MappedProperty[T, P, Src](f: P => T, g: T => Option[P], parent: Property[P, Src]) extends Property[T, Src] {
  def get(e: Src): Validation[NonEmptyList[String], T] = parent.get(e) map f
  def put(t: T, e: Src) = parent.put(g(t).get, e)
}

trait P1[A, Src] extends Property[A, Src] {  
  def <>[Z](f: (A => Z), g: (Z => Some[A])) = MappedProperty[Z, A, Src](f, g, this)
  
  def ~[B](prop: P1[B, Src]) = new P2[A,B, Src] {
    def get(e: Src) = P1.this.get(e) <|*|> prop.get(e)
    
    def put(t2: (A, B), e: Src) = prop.put(t2._2, P1.this.put(t2._1, e))
  }
}

trait P2[A, B, Src] extends Property[(A, B), Src] {
  def <>[Z](f: ((A, B) => Z), g: (Z => Some[(A, B)])) = MappedProperty[Z, (A,B), Src](f.tupled, g, this)
}

trait P3[A, B, C, Src] extends Property[(A, B, C), Src] {
  def <>[Z](f: ((A, B, C) => Z), g: (Z => Some[(A, B, C)])) = MappedProperty[Z, (A, B, C), Src](f.tupled, g, this)
}
