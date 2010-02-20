package sage

import props._
import scalaz._
import Scalaz._
import com.google.appengine.api.datastore._

trait EntityProperties {
  implicit def newtypeProp[T, U <: NewType[T]](f: T => U)(implicit m: ClassManifest[U], tm: ClassManifest[T]): Property[Entity, U] =
    stringProp(m.erasure.getSimpleName.toLowerCase).typedProp[U, T](f)
  
  implicit def stringProp(s: String) = new {
    def typedProp[T <: NewType[U], U](f: U => T)(implicit m: ClassManifest[U]): Property[Entity, T] = 
      prop[U].><(f, ((_: NewType[U]).value))
    
    def prop[T : ClassManifest] = forString[T](s)
  } 
  
  def forString[T : ClassManifest](s: String): Property[Entity, T] = new Property[Entity, T] {
    def get(e: Entity) = e.property[T](s).toSuccess(missing(s).wrapNel)
    
    def put(t: T, e: Entity) = {
      e.setProperty(s, t)
      e
    }
  }
}