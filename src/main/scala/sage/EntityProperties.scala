package sage

import hprops._
import scalaz._
import Scalaz._
import com.google.appengine.api.datastore._

object EntityPropBuilders {
  def string[T : ClassManifest](s: String): ReadWrite[Entity, T] = new ReadWrite[Entity, T] {
    def get(e: Entity) = e.property[T](s).toSuccess(missing(s).wrapNel)
    
    def put(t: T, e: Entity) = {
      e.setProperty(s, t)
      success(e)
    }
  }
  
  def newType[T, U <: NewType[T]](fieldName: String, f: T => U)(implicit m: ClassManifest[T]) = 
    string[T](fieldName) >< (f, ((_: NewType[T]).value))
}

trait StringW {
  val s: String
  def typedProp[T, U <: NewType[T]](f: T => U)(implicit m: ClassManifest[T]): ReadWrite[Entity, U] = 
    EntityPropBuilders.newType[T, U](s, f)
  
  def prop[T : ClassManifest] = EntityPropBuilders.string[T](s) 
}

trait NewTypeConsW[T, U <: NewType[T]] {
  val f: T => U
  
  def prop(implicit mu: ClassManifest[U], mt: ClassManifest[T]) = {
    val thisName = mu.erasure.getSimpleName.toLowerCase
    EntityPropBuilders.newType[T, U](thisName, f)
  }
}

trait EntityProperties {
  implicit def newTypeConsProp[T, U <: NewType[T]](g: T => U) = new NewTypeConsW[T, U] {
    val f = g
  }  
  
  implicit def stringProp(str: String) = new StringW { val s = str }   
}
