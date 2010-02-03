import scalaz._
import Scalaz._
import com.google.appengine.api.datastore._

package object brotable {  
  implicit def stringProp(s: String) = new {
    
    def typedProp[T <: NewType[U], U <: AnyRef](f: U => T)(implicit m: ClassManifest[U]) = new P1[T] {
      val delegate = prop[U]
      
      def get(e: Entity) = delegate.get(e) map (u => f(u))
        
      
      def put(t: T, e: Entity) = delegate.put(t.value, e)
    }
    
    def prop[T <: AnyRef](implicit m: ClassManifest[T]) = new P1[T] {
      def get(e: Entity) = {
        Option(e.getProperty(s)) match {
          case v@Some(a: T) if (m.erasure.isAssignableFrom(a.getClass)) => v.asInstanceOf[Option[T]]
          case _ => None
        }
      }
      def put(t: T, e: Entity) = {
        e.setProperty(s, t)
        e
      }
    }
  }
}
