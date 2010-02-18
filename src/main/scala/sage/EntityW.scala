package sage

import scalaz._
import Scalaz._
import com.google.appengine.api.datastore._

object Utils {
  import java.lang.{Long => JLong}
  def typeCheck[T](a: AnyRef)(implicit m: ClassManifest[T]): Option[T] = a match { 
    case (a: JLong) if m.erasure == classOf[Long] => some(a.asInstanceOf[JLong].longValue.asInstanceOf[T])
    case a if m.erasure.isAssignableFrom(a.getClass) => some(a.asInstanceOf[T])
    case _ => none
  }
}

trait EntityW {
  val entity: Entity
  
  def property[T](key: String)(implicit m: ClassManifest[T]) =
    Option(entity.getProperty(key)) >>= Utils.typeCheck[T]
}

trait EntityImplicits {
  implicit def EntityTo(e: Entity): EntityW = new EntityW { val entity = e }
  implicit def EntityFrom(ew: EntityW): Entity = ew.entity
}
