package sage

import com.google.appengine.api.datastore._
import scalaz._
import Scalaz._

case class Keyed[T](key: Key, value: T)
